from drf_yasg2 import openapi
from drf_yasg2.errors import SwaggerGenerationError
from drf_yasg2.inspectors import SwaggerAutoSchema
from rest_framework.request import is_form_media_type
from drf_yasg2.utils import merge_params, swagger_auto_schema


token_param_config = openapi.Parameter(
    'Authorization', in_=openapi.IN_HEADER, description='token', type=openapi.TYPE_STRING)


class TokenAuthSwaggerAutoSchema(SwaggerAutoSchema):
    def add_manual_parameters(self, parameters):
        """Add/replace parameters from the given list of automatically generated request parameters.

        :param list[openapi.Parameter] parameters: genereated parameters
        :return: modified parameters
        :rtype: list[openapi.Parameter]
        """
        manual_parameters = self.overrides.get("manual_parameters", None) or []

        if self.view.permission_classes[0].__name__ == 'IsAuthenticated':
            manual_parameters.append(token_param_config)

        if any(
                param.in_ == openapi.IN_BODY for param in manual_parameters
        ):  # pragma: no cover
            raise SwaggerGenerationError(
                "specify the body parameter as a Schema or Serializer in request_body"
            )
        if any(
                param.in_ == openapi.IN_FORM for param in manual_parameters
        ):  # pragma: no cover
            has_body_parameter = any(
                param.in_ == openapi.IN_BODY for param in parameters
            )
            if has_body_parameter or not any(
                    is_form_media_type(encoding) for encoding in self.get_consumes()
            ):
                raise SwaggerGenerationError(
                    "cannot add form parameters when the request has a request body; "
                    "did you forget to set an appropriate parser class on the view?"
                )
            if self.method not in self.body_methods:
                raise SwaggerGenerationError(
                    "form parameters can only be applied to "
                    "(" + ",".join(self.body_methods) + ") HTTP methods"
                )

        return merge_params(parameters, manual_parameters)


token_auth_auto_schema = swagger_auto_schema(auto_schema=TokenAuthSwaggerAutoSchema)
