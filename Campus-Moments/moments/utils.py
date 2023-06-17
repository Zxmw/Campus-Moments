from rest_framework.filters import OrderingFilter, BaseFilterBackend
from django.db.models import Q
from rest_framework.compat import coreapi, coreschema
from django.utils.encoding import force_str


class OrderingFilterWithSchema(OrderingFilter):
    def get_schema_fields(self, view):
        self.ordering_description = "ordering choices: " + ', '.join(view.ordering_fields) + '\n'
        return super().get_schema_fields(view)


class FollowMomentsFilterBackend(BaseFilterBackend):
    def filter_queryset(self, request, queryset, view):
        usr = request.user
        following = request.query_params.get('following')
        if following:
            return queryset.filter(Q(user__in=usr.follow_list.all()))
        return queryset

    def get_schema_fields(self, view):
        assert coreapi is not None, 'coreapi must be installed to use `get_schema_fields()`'
        assert coreschema is not None, 'coreschema must be installed to use `get_schema_fields()`'
        return [
            coreapi.Field(
                name="following",
                required=False,
                location='query',
                schema=coreschema.String(
                    title=force_str(""),
                    description=force_str("Filter by following")
                )
            )
        ]
