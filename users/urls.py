from django.urls import path
from .views import *
from knox import views as knox_views
from drf_yasg2.utils import swagger_auto_schema
from .utils import token_param_config


# decorated_logout_view = \
#    swagger_auto_schema(
#       method='post',
#       manual_parameters=[token_param_config],
#    )(knox_views.LogoutView.as_view())

urlpatterns = [
    path('api/register', RegisterView.as_view(), name="register"),
    path('api/login', LoginView.as_view(), name="login"),
    path('api/self', UserView.as_view(), name="self"),
    path('api/logout', knox_views.LogoutView.as_view(), name="logout"),
    path('api/users', UserListAPIView.as_view(),
         name='users'),
    path('api/users/<int:pk>', UserDetailAPIView.as_view()),
    path('api/follow-block', FollowBlockAPIView.as_view(),
         name='follow-block'),
    #path('api/logout', decorated_logout_view, name="logout"),
]

