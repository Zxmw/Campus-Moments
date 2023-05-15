from rest_framework import serializers
from moments.models import Moment, Comment
from users.models import MyUser
from .models import *


class MessageSerializer(serializers.ModelSerializer):
    class Meta:
        model = Message
        fields = '__all__'


