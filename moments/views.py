from rest_framework.generics import ListCreateAPIView, RetrieveUpdateDestroyAPIView
from .serializers import *
from .models import *
from rest_framework import permissions
from rest_framework.parsers import MultiPartParser, FormParser
from django_filters.rest_framework import DjangoFilterBackend
from .utils import *


# Create your views here.
class MomentListAPIView(ListCreateAPIView):
    serializer_class = MomentSerializer
    queryset = Moment.objects.all()
    permission_classes = (permissions.IsAuthenticated,)
    filter_backends = (OrderingFilterWithSchema,)
    parser_classes = [MultiPartParser, FormParser]

    # Explicitly specify which fields the API may be ordered against
    ordering_fields = ('created_at',)

    # This will be used as the default ordering
    ordering = '-created_at'


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
