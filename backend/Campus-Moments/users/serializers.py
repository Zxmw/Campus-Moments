from django.contrib.auth import authenticate
from django.contrib.auth.tokens import PasswordResetTokenGenerator
from django.utils.encoding import force_str
from django.utils.http import urlsafe_base64_decode
from rest_framework import serializers
from rest_framework.exceptions import AuthenticationFailed

from users.models import MyUser


class UserSerializer(serializers.ModelSerializer):
    username = serializers.CharField(required=False)

    follow_list = serializers.PrimaryKeyRelatedField(
        many=True,
        read_only=True
    )
    block_list = serializers.PrimaryKeyRelatedField(
        many=True,
        read_only=True
    )
    followers = serializers.PrimaryKeyRelatedField(
        many=True,
        read_only=True
    )

    class Meta:
        model = MyUser
        fields = ['id', 'username', 'avatar', 'bio', 'follow_list', 'block_list', 'email', 'followers']


class RegisterSerializer(serializers.ModelSerializer):
    class Meta:
        model = MyUser
        fields = ['username', 'password', 'email']
        extra_kwargs = {
            'password': {'write_only': True}
        }

    def create(self, validated_data):
        return MyUser.objects.create_user(**validated_data)


class LoginSerializer(serializers.Serializer):
    username = serializers.CharField()
    password = serializers.CharField()

    def validate(self, data):
        user = authenticate(**data)
        if user and user.is_active:
            return user
        raise serializers.ValidationError("Incorrect Credentials")


class FollowBlockSerializer(serializers.Serializer):
    id = serializers.IntegerField()
    list = serializers.ChoiceField(choices=(('follow', 'follow'), ('block', 'block')))
    action = serializers.ChoiceField(choices=(('add', 'add'), ('remove', 'remove')))

    def validate(self, data):
        try:
            user = MyUser.objects.get(pk=data.get('id'))
            list = data.get('list')
            action = data.get('action')
            return user, list, action
        except:
            raise serializers.ValidationError("user do not exist")


class ResetPasswordEmailRequestSerializer(serializers.Serializer):
    email = serializers.EmailField(min_length=2)

    redirect_url = serializers.CharField(max_length=500, required=False)

    class Meta:
        fields = ['email']


class SetNewPasswordSerializer(serializers.Serializer):
    password = serializers.CharField(
        min_length=6, max_length=68, write_only=True)
    token = serializers.CharField(
        min_length=1, write_only=True)
    uidb64 = serializers.CharField(
        min_length=1, write_only=True)

    class Meta:
        fields = ['password', 'token', 'uidb64']

    def validate(self, attrs):
        try:
            password = attrs.get('password')
            token = attrs.get('token')
            uidb64 = attrs.get('uidb64')

            print(password)

            id = force_str(urlsafe_base64_decode(uidb64))
            user = MyUser.objects.get(id=id)
            if not PasswordResetTokenGenerator().check_token(user, token):
                raise AuthenticationFailed('The reset link is invalid', 401)

            user.set_password(password)
            user.save()
            print(user)
            print(user.password)

            return user
        except Exception as e:
            raise AuthenticationFailed('The reset link is invalid', 401)
        return super().validate(attrs)
