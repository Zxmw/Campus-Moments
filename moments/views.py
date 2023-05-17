from rest_framework.generics import ListCreateAPIView, RetrieveUpdateDestroyAPIView, GenericAPIView
from .serializers import *
from .models import *
from rest_framework import permissions
from rest_framework.parsers import MultiPartParser, FormParser
from django_filters.rest_framework import DjangoFilterBackend
from .utils import *
from django.db.models import Count
from rest_framework import response, status


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
            annotate(total_stars=Count('stared_by', distinct=True)). \
            annotate(total_comments=Count('comments', distinct=True)).all()
        return queryset


class MomentDetailAPIView(RetrieveUpdateDestroyAPIView):
    serializer_class = MomentSerializer
    queryset = Moment.objects.all()
    permission_classes = (permissions.IsAuthenticated,)

    def get_queryset(self):
        queryset = Moment.objects.annotate(total_likes=Count('liked_by', distinct=True)). \
            annotate(total_stars=Count('stared_by', distinct=True)). \
            annotate(total_comments=Count('comments', distinct=True)).all()
        return queryset


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


class LikeStarAPIView(GenericAPIView):
    serializer_class = LikeStarSerializer

    def post(self, request):
        serializer = self.serializer_class(data=request.data)
        if serializer.is_valid():
            moment = Moment.objects.get(id=serializer.validated_data[0])
            if serializer.validated_data[1] == 'like':
                if serializer.validated_data[2] == 'add':
                    moment.liked_by.add(request.user)
                else:
                    moment.liked_by.remove(request.user)
            else:
                if serializer.validated_data[2] == 'star':
                    moment.stared_by.add(request.user)
                else:
                    moment.stared_by.remove(request.user)
            request.user.save()
            return response.Response({"follow_list": request.user.follow_list.all().values_list('id', flat=True),
                                      "block_list": request.user.block_list.all().values_list('id', flat=True)},
                                     status=status.HTTP_200_OK)
        return response.Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
