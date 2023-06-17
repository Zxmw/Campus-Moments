from django.db import models
from users.models import MyUser


class Message(models.Model):
    time = models.DateTimeField(auto_now_add=True)
    sender = models.ForeignKey(to=MyUser, on_delete=models.CASCADE, related_name='sent_messages')
    receiver = models.ForeignKey(to=MyUser, on_delete=models.CASCADE, related_name='received_messages')
    content = models.TextField()
