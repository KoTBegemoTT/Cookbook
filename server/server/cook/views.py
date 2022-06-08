from django.core.files.storage import FileSystemStorage
from rest_framework import filters, permissions, status
from rest_framework.generics import get_object_or_404
from rest_framework.viewsets import ModelViewSet

from coock.logic import set_rating, getNeironString
from coock.recipe2ingredient.main import getPrediction, predict_image_without_path
from coock.models import Dish, UserDishRelationship, Preview, Categorie, Step
from coock.serializers import DishSerializer, UserDishSerializer, PreviewSerializer, CategorySerializer, StepSerializer

from rest_framework.response import Response
from rest_framework.views import APIView


class DishView(APIView):
    def get(self, request, pk):
        dishes = get_object_or_404(Dish.objects.all(), pk=pk)
        serializer = DishSerializer(dishes, many=False)
        return Response(serializer.data)


class PreviewView(APIView):
    def get(self, request):
        dishes = Preview.objects.all()
        serializer = PreviewSerializer(dishes, many=True)
        return Response(serializer.data)


class PopularDish(APIView):
    def get(self, request):
        dishes = Preview.objects.order_by('-rating')
        serializer = PreviewSerializer(dishes, many=True)
        return Response(serializer.data)

class FindDish(APIView):
    def get(self, request, pk):
        data = pk
        ingredients = data.split('&')
        dishes = Dish.objects.all()
        dishDev = []
        for d in dishes:
            deviation = 0

            currentIngredients = d.ingredients_name.split('&')
            if len(currentIngredients) > len(ingredients):
                deviation = len(currentIngredients) - len(ingredients)
            for i in ingredients:
                if i not in currentIngredients:
                    deviation += 2

            dishDev.append(ClosenessDish(d.pk, deviation))

        dishDev = sorted(dishDev, key=lambda x: x.deviation)
        for d in dishDev:
            print(d.deviation)
        dishes = Dish.objects.filter(id=dishDev[0].pk) | Dish.objects.filter(id=dishDev[1].pk)
        serializer = DishSerializer(dishes, many=True)
        return Response(serializer.data)

class dishAtCaegory(APIView):
    def get(self, request, pk):
        dishes = Preview.objects.filter(category=pk)
        serializer = PreviewSerializer(dishes, many=True)
        return Response(serializer.data)


class DishSteps(APIView):
    def get(self, request, pk):
        steps = Step.objects.filter(dish=pk).order_by('number')
        serializer = StepSerializer(steps, many=True)
        return Response(serializer.data)


class CategoryView(APIView):
    def get(self, request):
        categories = Categorie.objects.all()
        serializer = CategorySerializer(categories, many=True)
        return Response(serializer.data)


class LovedView(APIView):
    permission_classes = [permissions.IsAuthenticated]

    def get(self, request):
        relations = UserDishRelationship.objects.filter(loved=True, user=request.user.id)
        previews = [r.dish for r in relations]
        serializer = PreviewSerializer(previews, many=True)
        return Response(serializer.data)


class NeironView(APIView):
    def post(self, request):
        # получаем загруженный файл
        file = request.FILES['image']
        fs = FileSystemStorage('media\\neiron')
        # сохраняем на файловой системе
        file_name = fs.save(file.name, file)
        return Response(getPrediction(fs.path(file_name), file_name), status=status.HTTP_201_CREATED)


class UserDishView(APIView):
    permission_classes = [permissions.IsAuthenticated]

    def get(self, request, pk):
        preview = get_object_or_404(Preview.objects.all(), dish=pk)
        relation = get_object_or_404(UserDishRelationship.objects.all(), dish=preview, user=request.user.id)
        serializer = UserDishSerializer(relation, many=False)
        return Response(serializer.data)

    def post(self, request, pk):
        relation = request.data.get('relation')
        relation['user'] = request.user.id
        preview = Preview.objects.get(dish=pk)
        relation['dish'] = preview.id
        serializer = UserDishSerializer(data=relation)
        if serializer.is_valid(raise_exception=True):
            new_relation = serializer.save()
        if relation['rate'] is not None:
            set_rating(new_relation.dish)
        return Response("Created successfully", status=status.HTTP_201_CREATED)

    def put(self, request, pk):
        preview = get_object_or_404(Preview.objects.all(), dish=pk)
        current_relation = get_object_or_404(UserDishRelationship.objects.all(), dish=preview, user=request.user.id)
        data = request.data.get('relation')
        serializer = UserDishSerializer(instance=current_relation, data=data, partial=True)
        if serializer.is_valid(raise_exception=True):
            new_relation = serializer.save()
        set_rating(new_relation.dish)
        return Response("Updated successfully", status=status.HTTP_200_OK)


class ClosenessDish:
    def __init__(self, pk, deviation):
        self.pk = pk
        self.deviation = deviation
