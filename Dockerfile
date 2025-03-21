FROM ubuntu:latest
LABEL authors="brandonpereira"

ENTRYPOINT ["top", "-b"]