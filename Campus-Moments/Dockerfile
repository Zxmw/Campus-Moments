from python:3.11

ENV PYTHONUNBUFFERED 1

RUN mkdir /code
WORKDIR /code

RUN pip install --default-timeout=100 --upgrade pip -i https://pypi.tuna.tsinghua.edu.cn/simple
RUN pip install uwsgi -i https://pypi.tuna.tsinghua.edu.cn/simple
ADD requirements.txt /code/
RUN pip install -r requirements.txt -i https://pypi.tuna.tsinghua.edu.cn/simple
ADD . /code/
EXPOSE 8000
