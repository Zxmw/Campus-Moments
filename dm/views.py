from rest_framework.generics import ListCreateAPIView, RetrieveUpdateDestroyAPIView
from .serializers import *
from .models import *
from rest_framework import permissions
from django_filters.rest_framework import DjangoFilterBackend


class MessageListAPIView(ListCreateAPIView):
    serializer_class = MessageSerializer
    queryset = Message.objects.all()
    permission_classes = (permissions.IsAuthenticated,)
    filter_backends = (DjangoFilterBackend,)

    filterset_fields = ['sender', 'receiver', ]


class MessageDetailAPIView(RetrieveUpdateDestroyAPIView):
    serializer_class = MessageSerializer
    queryset = Message.objects.all()
    permission_classes = (permissions.IsAuthenticated,)



