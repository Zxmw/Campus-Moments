from django.urls import path
from .views import *



urlpatterns = [
    path('api/messages', MessageListAPIView.as_view(),
         name='massages'),
    path('api/messages/<int:pk>', MessageDetailAPIView.as_view(),
         name='message'),
]