from abc import ABC

from django.contrib.auth import authenticate
from rest_framework import serializers

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
        fields = ['id', 'username', 'avatar', 'bio', 'follow_list', 'block_list', 'email','followers']


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
            return (user, list, action)
        except:
            raise serializers.ValidationError("user do not exist")
