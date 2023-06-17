import os

from django.http import HttpResponsePermanentRedirect
from django.utils.encoding import smart_str, smart_bytes, DjangoUnicodeDecodeError
from django.utils.http import urlsafe_base64_encode
from django_filters.rest_framework import DjangoFilterBackend
from knox.models import AuthToken
from rest_framework import response, status, permissions
from rest_framework.generics import GenericAPIView, RetrieveAPIView, ListCreateAPIView, RetrieveUpdateDestroyAPIView
from rest_framework.parsers import MultiPartParser, FormParser
from django.contrib.sites.shortcuts import get_current_site
from django.urls import reverse
from .utils import Util

from users.serializers import *
from .models import MyUser


class CustomRedirect(HttpResponsePermanentRedirect):
    allowed_schemes = [os.environ.get('APP_SCHEME'), 'http', 'https']


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
    filter_backends = [DjangoFilterBackend]

    filterset_fields = {'id': ["in", "exact"]}

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


class RequestPasswordResetEmail(GenericAPIView):
    serializer_class = ResetPasswordEmailRequestSerializer

    def post(self, request):
        serializer = self.serializer_class(data=request.data)

        email = request.data.get('email', '')

        if MyUser.objects.filter(email=email).exists():
            user = MyUser.objects.get(email=email)
            uidb64 = urlsafe_base64_encode(smart_bytes(user.id))
            token = PasswordResetTokenGenerator().make_token(user)
            # current_site = get_current_site(
            #     request=request).domain
            # relativeLink = reverse(
            #     'password-reset-confirm', kwargs={'uidb64': uidb64, 'token': token})
            #
            # redirect_url = request.data.get('redirect_url', '')
            # absurl = 'http://' + current_site + relativeLink
            email_body = f'uidb64: {uidb64}\ntoken: {token}'
            data = {'email_body': email_body, 'to_email': user.email,
                    'email_subject': 'Reset your passsword'}
            Util.send_email(data)
        return response.Response({'success': 'We have sent you a link to reset your password'},
                                 status=status.HTTP_200_OK)



class PasswordTokenCheckAPI(GenericAPIView):
    serializer_class = SetNewPasswordSerializer

    def get(self, request, uidb64, token):

        redirect_url = request.GET.get('redirect_url')

        try:
            return CustomRedirect(
                redirect_url + '?uidb64=' + uidb64 + '&token=' + token)

        except:
            return response.Response({'error': 'something wrong'},
                                     status=status.HTTP_400_BAD_REQUEST)


class SetNewPasswordAPIView(GenericAPIView):
    serializer_class = SetNewPasswordSerializer

    def patch(self, request):
        serializer = self.serializer_class(data=request.data)
        if serializer.is_valid():
            return response.Response({'success': True, 'message': 'Password reset success'}, status=status.HTTP_200_OK)
        return response.Response(serializer.errors, status=status.HTTP_401_UNAUTHORIZED)
