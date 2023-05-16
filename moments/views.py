from rest_framework.generics import ListCreateAPIView, RetrieveUpdateDestroyAPIView
from .serializers import *
from .models import *
from rest_framework import permissions
from rest_framework.parsers import MultiPartParser, FormParser
from django_filters.rest_framework import DjangoFilterBackend
from .utils import *
from django.db.models import Count


# Create your views here.
class MomentListAPIView(ListCreateAPIView):
    serializer_class = MomentSerializer
    queryset = Moment.objects.all()
    permission_classes = (permissions.IsAuthenticated,)
    filter_backends = (OrderingFilterWithSchema, DjangoFilterBackend, FollowMomentsFilterBackend)
    parser_classes = [MultiPartParser, FormParser]

    # Explicitly specify which fields the API may be ordered against
    ordering_fields = ('created_at', 'total_likes', 'total_comments')

    # This will be used as the default ordering
    ordering = '-created_at'

    filterset_fields = ['tag', 'user__id']

    def get_queryset(self):
        queryset = Moment.objects.annotate(total_likes=Count('liked_by', distinct=True)). \
            annotate(total_stars=Count('stared_by',  distinct=True)). \
            annotate(total_comments=Count('comments',  distinct=True)).all()
        return queryset


class MomentDetailAPIView(RetrieveUpdateDestroyAPIView):
    serializer_class = MomentSerializer
    queryset = Moment.objects.all()
    permission_classes = (permissions.IsAuthenticated,)


class CommentListAPIView(ListCreateAPIView):
    serializer_class = CommentSerializer
    queryset = Comment.objects.all()
    permission_classes = (permissions.IsAuthenticated,)
    filter_backends = (OrderingFilterWithSchema,)

    # Explicitly specify which fields the API may be ordered against
    ordering_fields = ('created_at',)

    # This will be used as the default ordering
    ordering = '-created_at'


class CommentDetailAPIView(RetrieveUpdateDestroyAPIView):
    serializer_class = CommentSerializer
    queryset = Comment.objects.all()
    permission_classes = (permissions.IsAuthenticated,)
