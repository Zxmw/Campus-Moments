from rest_framework import serializers
from moments.models import Moment, Comment
from users.models import MyUser


class MomentSerializer(serializers.ModelSerializer):
    class Meta:
        model = Moment
        fields = '__all__'


class CommentSerializer(serializers.ModelSerializer):
    class Meta:
        model = Comment
        fields = '__all__'
        # depth = 1
