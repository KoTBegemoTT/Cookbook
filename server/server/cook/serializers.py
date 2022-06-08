from rest_framework.serializers import ModelSerializer

from coock.models import Dish, UserDishRelationship, Preview, Categorie, Step


class DishSerializer(ModelSerializer):
    class Meta:
        model = Dish
        fields = "__all__"


class StepSerializer(ModelSerializer):
    class Meta:
        model = Step
        fields = "__all__"


class CategorySerializer(ModelSerializer):
    class Meta:
        model = Categorie
        fields = "__all__"


class PreviewSerializer(ModelSerializer):
    class Meta:
        model = Preview
        fields = "__all__"


class UserDishSerializer(ModelSerializer):
    class Meta:

        model = UserDishRelationship
        fields = "__all__"

    def create(self, validated_data):
        return UserDishRelationship.objects.create(**validated_data)

    def update(self, instance, validated_data):
        instance.rate = validated_data.get('rate', instance.rate)
        instance.loved = validated_data.get('loved', instance.loved)
        instance.save()
        return instance
