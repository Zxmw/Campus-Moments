import os

from django.contrib.auth.tokens import PasswordResetTokenGenerator
from django.utils.encoding import smart_str, smart_bytes, DjangoUnicodeDecodeError
from django.utils.http import urlsafe_base64_decode, urlsafe_base64_encode
from django.utils.decorators import method_decorator
from knox.models import AuthToken
from rest_framework import response, status, permissions
from rest_framework.generics import GenericAPIView, RetrieveAPIView, ListCreateAPIView, RetrieveUpdateDestroyAPIView
from rest_framework.parsers import MultiPartParser, FormParser

from users.serializers import *
from .models import MyUser


# Create your views here.
class RegisterView(GenericAPIView):
    serializer_class = RegisterSerializer

    def post(self, request):
        serializer = self.serializer_class(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return response.Response(status=status.HTTP_201_CREATED)
        return response.Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)


class LoginView(GenericAPIView):
    serializer_class = LoginSerializer

    def post(self, request):
        serializer = self.serializer_class(data=request.data)
        if serializer.is_valid():
            user = serializer.validated_data
            return response.Response({"user": user.id,
                                      "token": AuthToken.objects.create(user)[1]},
                                     status=status.HTTP_200_OK)
        return response.Response(serializer.errors, status=status.HTTP_401_UNAUTHORIZED)


class UserView(RetrieveAPIView):
    permission_classes = [
        permissions.IsAuthenticated,
    ]
    serializer_class = UserSerializer

    def get_object(self):
        return self.request.user


class UserListAPIView(ListCreateAPIView):
    serializer_class = UserSerializer
    queryset = MyUser.objects.all()
    permission_classes = (permissions.IsAuthenticated,)

    def perform_create(self, serializer):
        user = serializer.save()
        user.set_password(self.request.POST['password'])
        user.save()


class UserDetailAPIView(RetrieveUpdateDestroyAPIView):
    parser_classes = [MultiPartParser, FormParser]
    serializer_class = UserSerializer
    queryset = MyUser.objects.all()


class FollowBlockAPIView(GenericAPIView):
    serializer_class = FollowBlockSerializer

    def post(self, request):
        serializer = self.serializer_class(data=request.data)
        if serializer.is_valid():
            if serializer.validated_data[1] == 'follow':
                if serializer.validated_data[2] == 'add':
                    request.user.follow_list.add(serializer.validated_data[0])
                else:
                    request.user.follow_list.remove(serializer.validated_data[0])
            else:
                if serializer.validated_data[2] == 'add':
                    request.user.block_list.add(serializer.validated_data[0])
                else:
                    request.user.block_list.remove(serializer.validated_data[0])
            request.user.save()
            return response.Response({"follow_list": request.user.follow_list.all().values_list('id', flat=True),
                                      "block_list": request.user.block_list.all().values_list('id', flat=True)},
                                     status=status.HTTP_200_OK)
        return response.Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
