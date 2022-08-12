#! /bin/bash

while [ true ]; do
	curl localhost:$1/whoami
done
