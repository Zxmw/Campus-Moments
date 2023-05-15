from rest_framework.filters import OrderingFilter


class OrderingFilterWithSchema(OrderingFilter):
    def get_schema_fields(self, view):
        self.ordering_description = "ordering choices: " + ', '.join(view.ordering_fields)+'\n'
        return super().get_schema_fields(view)
