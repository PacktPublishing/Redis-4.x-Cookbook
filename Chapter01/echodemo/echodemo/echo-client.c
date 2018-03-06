#include <string.h>
#include <stdio.h>
#include "../src/ae.h"
#include "../src/anet.h"
#include "echo.h"


char myerr[ANET_ERR_LEN] = {0};

int main() {
    aeEventLoop *el = aeCreateEventLoop(1024);

    int fd = anetTcpConnect(myerr, "127.0.0.1", 8000);

    char writebuff[20] = {0};
    const char *ptr = "hello!";
    sprintf(writebuff, ptr, strlen(ptr));
    anetWrite(fd, writebuff, strlen(ptr));

    anetNonBlock(myerr, fd);
    anetEnableTcpNoDelay(myerr, fd);
    aeCreateFileEvent(el, fd, AE_READABLE, readProc, el);

    printf("Client started: \n");
    aeMain(el);
    aeDeleteEventLoop(el);


    return 0;
}
