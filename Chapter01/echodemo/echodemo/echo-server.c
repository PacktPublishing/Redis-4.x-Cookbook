//
// Created by gnuhpc on 9/2/17.
//
#include <stdio.h>
#include <assert.h>
#include <errno.h>
#include "../src/ae.h"
#include "../src/anet.h"

char myerr[ANET_ERR_LEN] = {0};
void acceptProc(struct aeEventLoop *eventLoop, int fd, void *clientdata, int mask);

int main() {
    aeEventLoop *el = aeCreateEventLoop(1024);
    if(!el){
        return 1;
    }
    int fd = anetTcpServer(myerr, 8000, "0.0.0.0", 511);

    if (fd != ANET_ERR) {
        anetNonBlock(NULL, fd);
        if (aeCreateFileEvent(el, fd, AE_READABLE, acceptProc, NULL)) {
            printf("Unrecoverable errror createing server file event");
        }
    } else if (errno == EAFNOSUPPORT) {
        printf("Not listening to IPv4: unsupported");
    }

    printf("Server started at 0.0.0.0:8000! \n");
    aeMain(el);
    aeDeleteEventLoop(el);
    return 0;
}
