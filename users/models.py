from django.core import validators
from django.contrib.auth.hashers import make_password
from django.contrib.auth.models import AbstractBaseUser, PermissionsMixin, BaseUserManager, User
from django.db import models
from django.utils import timezone
from django.utils.translation import gettext_lazy as _


# def upload_to(instance, dirname, filename):
#     return '{dirname}/{filename}'.format(filename=filename)


class MyUserManager(BaseUserManager):

    def _create_user(self, username, password, email, **extra_fields):

        if not username:
            raise ValueError("The given username must be set")
        email = self.normalize_email(email)
        if not email:
            raise ValueError('User should have a email')
        user = self.model(username=username, email=email, **extra_fields)
        user.password = make_password(password)
        user.save(using=self._db)
        return user

    def create_user(self, username, password, email, **extra_fields):
        extra_fields.setdefault('is_staff', False)
        extra_fields.setdefault('is_superuser', False)
        return self._create_user(username, password, email, **extra_fields)

    def create_superuser(self, username, password, email, **extra_fields):
        extra_fields.setdefault('is_staff', True)
        extra_fields.setdefault('is_superuser', True)

        if extra_fields.get('is_staff') is not True:
            raise ValueError('Superuser must have is_staff=True.')
        if extra_fields.get('is_superuser') is not True:
            raise ValueError('Superuser must have is_superuser=True.')

        return self._create_user(username, password, email, **extra_fields)


class MyUser(AbstractBaseUser, PermissionsMixin):
    """
    An abstract base class implementing a fully featured User model with
    admin-compliant permissions.

    Username and password are required. Other fields are optional.
    """
    username_validator = validators.RegexValidator(regex=r"^[\S.@+-]+$", message=_("Enter a valid username."))

    username = models.CharField(
        _("username"),
        max_length=150,
        unique=True,
        help_text=_(
            "Required. 150 characters or fewer. Letters, digits and @/./+/-/_ only."
        ),
        validators=[username_validator],
        error_messages={
            "unique": _("A user with that username already exists."),
        },
    )
    email = models.EmailField(_("email address"), null=True, blank=True, unique=True)
    avatar = models.ImageField(_("Image"), upload_to='avatars', null=True, blank=True)
    bio = models.TextField(blank=True)
    is_staff = models.BooleanField(
        _("staff status"),
        default=False,
        help_text=_("Designates whether the user can log into this admin site."),
    )
    is_active = models.BooleanField(
        _("active"),
        default=True,
        help_text=_(
            "Designates whether this user should be treated as active. "
            "Unselect this instead of deleting accounts."
        ),
    )
    date_joined = models.DateTimeField(_("date joined"), default=timezone.now)
    follow_list = models.ManyToManyField("self", related_name='followers', symmetrical=False, blank=True)
    block_list = models.ManyToManyField("self", related_name='blocked_by', symmetrical=False, blank=True)

    objects = MyUserManager()

    EMAIL_FIELD = "email"
    USERNAME_FIELD = "username"
    REQUIRED_FIELDS = ['email']

    def __str__(self):
        return str(self.username)
