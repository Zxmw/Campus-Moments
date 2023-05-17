from rest_framework import serializers
from moments.models import Moment, Comment
from users.serializers import UserSerializer


class MomentSerializer(serializers.ModelSerializer):
    total_likes = serializers.IntegerField(read_only=True)
    total_stars = serializers.IntegerField(read_only=True)
    total_comments = serializers.IntegerField(read_only=True)
    comments = serializers.PrimaryKeyRelatedField(many=True, read_only=True)

    class Meta:
        model = Moment
        fields = '__all__'



class CommentSerializer(serializers.ModelSerializer):

    class Meta:
        model = Comment
        fields = '__all__'
        # depth = 1


class LikeStarSerializer(serializers.Serializer):
    moment_id = serializers.IntegerField()
    list = serializers.ChoiceField(choices=(('like', 'like'), ('star', 'star')))
    action = serializers.ChoiceField(choices=(('add', 'add'), ('remove', 'remove')))

    def validate(self, data):
        try:
            moment = Moment.objects.get(pk=data.get('id'))
            list = data.get('list')
            action = data.get('action')
            return moment, list, action
        except:
            raise serializers.ValidationError("moment do not exist")
