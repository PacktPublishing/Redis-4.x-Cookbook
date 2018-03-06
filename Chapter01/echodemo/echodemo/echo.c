//
// Created by gnuhpc on 9/2/17.
//

#include <string.h>
#include <sys/socket.h>
#include <stdlib.h>
#include <stdio.h>
#include <zconf.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <errno.h>
#include "../src/ae.h"
#include "../src/anet.h"


void unlinkFileEvent(aeEventLoop *loop, int fd) {

    if (fd != -1) {
        aeDeleteFileEvent(loop, fd, AE_READABLE);
        aeDeleteFileEvent(loop, fd, AE_WRITABLE);
        close(fd);
        fd = -1;
    }
}

void writeProc(aeEventLoop *loop, int fd, void *clientdata, int mask)
{

    struct sockaddr_in guest;
    char guest_ip[20];
    int guest_len = sizeof(guest);
    getpeername(fd,(struct sockaddr*)&guest,&guest_len);
    inet_ntop(AF_INET,(void *)&guest.sin_addr, guest_ip,sizeof(guest_ip));
    printf("Sending Client data to %s:%d!\n", guest_ip, guest.sin_port);
    char *buffer = clientdata;

    int n_read = anetWrite(fd, buffer, strlen(buffer));
    printf("Size of Sent Data:%d, and the Data is: %s \n", n_read, buffer);

    free(buffer);
    aeDeleteFileEvent(loop, fd, mask);
}


void readProc(struct aeEventLoop *eventLoop, int fd, void *clientdata, int mask) {
    struct sockaddr_in guest;
    char guest_ip[20];
    int guest_len = sizeof(guest);
    getpeername(fd,(struct sockaddr*)&guest,&guest_len);
    inet_ntop(AF_INET,(void *)&guest.sin_addr, guest_ip,sizeof(guest_ip));
    printf("Reading Client data from %s:%d!\n", guest_ip, guest.sin_port);

    int buffer_size = 1024;
    char *buffer = malloc(sizeof(char) * buffer_size);
    bzero(buffer, buffer_size);
    int n_read = 0;

    n_read = read(fd, buffer, 1024);


    if (n_read == 0) {
        printf("Server close socket -- \n");
        unlinkFileEvent(eventLoop,fd);
    }

    if (n_read == -1) {
        printf("Socket read error \n");
        if(errno == EAGAIN){
            return;
        } else {
            unlinkFileEvent(eventLoop,fd);
            return;
        }
    }

    printf("Size of Data to be writen:%d, and the Data is: %s \n", n_read, buffer);
    aeCreateFileEvent(eventLoop, fd, AE_WRITABLE, writeProc, buffer);
//    write(fd,writebuff,n_read);
}

void acceptProc(struct aeEventLoop *eventLoop, int fd, void *clientdata, int mask) {
    char myerr[ANET_ERR_LEN] = {0};
    printf("Accept new connection in acceptProc.\n");
    char ip[20] = {0};
    int port = 0;
    int clientfd = anetTcpAccept(myerr, fd, ip, sizeof(ip), &port);

    if (clientfd == AE_ERR) {
        printf("acceptProc error occured!! \n");
        return;
    }

    printf("Client info - ip %s port %d \n", ip, port);


    int ret = anetNonBlock(myerr, clientfd);
    if (ret == ANET_OK) {
        printf("AnetNonBlock running successfully\n\n");
    }
    anetEnableTcpNoDelay(myerr, clientfd);
    if(aeCreateFileEvent(eventLoop, clientfd, AE_READABLE, readProc, NULL)==AE_ERR){
        close(fd);
        return;
    }
    write(clientfd,"Hello Client!\n",14);
}


