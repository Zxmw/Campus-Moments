from django.db import models
from users.models import MyUser


class Moment(models.Model):
    created_at = models.DateTimeField(auto_now_add=True)
    user = models.ForeignKey(to=MyUser, on_delete=models.CASCADE)
    tag = models.CharField(max_length=200, blank=True)
    title = models.CharField(max_length=200)
    content = models.TextField(blank=True)
    image = models.ImageField(upload_to='imgs', blank=True, null=True)
    video = models.FileField(upload_to='videos', blank=True, null=True)
    address = models.CharField(max_length=200, blank=True)
    liked_by = models.ManyToManyField(MyUser, related_name='liked_moments', blank=True)
    stared_by = models.ManyToManyField(MyUser, related_name='stared_moments', blank=True)


class Comment(models.Model):
    by = models.ForeignKey(to=MyUser, on_delete=models.CASCADE)
    moment = models.ForeignKey(to=Moment, on_delete=models.CASCADE, related_name='comments')
    content = models.TextField()
    created_at = models.DateTimeField(auto_now_add=True)


