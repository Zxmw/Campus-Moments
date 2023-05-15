from django.urls import path
from .views import *



urlpatterns = [
    path('api/moments', MomentListAPIView.as_view(),
         name='imgs'),
    path('api/moments/<int:pk>', MomentDetailAPIView.as_view(),
         name='moment'),
    path('api/comments', CommentListAPIView.as_view(),
         name='comments'),
    path('api/comments/<int:pk>', CommentDetailAPIView.as_view(),
         name='comments'),
]