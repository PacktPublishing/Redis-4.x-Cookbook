//
// Created by gnuhpc on 9/2/17.
//

#ifndef REDIS_ECHO_H
#define REDIS_ECHO_H


void readProc(struct aeEventLoop *eventLoop, int fd, void *clientdata, int mask);
void writeProc(struct aeEventLoop *eventLoop, int fd, void *clientdata, int mask);


#endif //REDIS_ECHO_H
