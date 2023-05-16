from rest_framework import serializers
from moments.models import Moment, Comment
from users.models import MyUser


class MomentSerializer(serializers.ModelSerializer):
    total_likes = serializers.IntegerField(read_only=True)
    total_stars = serializers.IntegerField(read_only=True)
    total_comments = serializers.IntegerField(read_only=True)

    class Meta:
        model = Moment
        fields = '__all__'

    def get_total_likes(self, obj):
        return obj.liked_by.count()

    def get_total_stars(self, obj):
        return obj.stared_by.count()

    def get_total_comments(self, obj):
        return obj.comments.count()






class CommentSerializer(serializers.ModelSerializer):
    class Meta:
        model = Comment
        fields = '__all__'
        # depth = 1
