from rest_framework import serializers
from moments.models import Moment, Comment
from users.models import MyUser


class ImageUrlField(serializers.RelatedField):
    def to_representation(self, instance):
        url = instance.user.avatar.url
        request = self.context.get('request', None)
        if request is not None:
            return request.build_absolute_uri(url)
        return url


class MomentSerializer(serializers.ModelSerializer):
    total_likes = serializers.IntegerField(read_only=True)
    total_stars = serializers.IntegerField(read_only=True)
    total_comments = serializers.IntegerField(read_only=True)
    comments = serializers.PrimaryKeyRelatedField(many=True, read_only=True)
    usr_username = serializers.SlugRelatedField(source='user',
                                                slug_field='username', read_only=True)
    usr_avatar = serializers.SerializerMethodField('get_usr_avatar')

    class Meta:
        model = Moment
        fields = '__all__'

    def get_usr_avatar(self, obj):
        url = obj.user.avatar.url if obj.user.avatar else ''
        request = self.context.get('request', None)
        if request is not None:
            return request.build_absolute_uri(url) if url else None
        return url


class CommentSerializer(serializers.ModelSerializer):
    usr_username = serializers.SlugRelatedField(source='by',
                                                slug_field='username', read_only=True)
    usr_avatar = serializers.SerializerMethodField('get_usr_avatar')

    class Meta:
        model = Comment
        fields = '__all__'
        # depth = 1

    def get_usr_avatar(self, obj):
        url = obj.by.avatar.url if obj.by.avatar else ''
        request = self.context.get('request', None)
        if request is not None:
            return request.build_absolute_uri(url) if url else None
        return url


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
