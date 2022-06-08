from django.urls import path

from coock.views import DishView, UserDishView, PreviewView, CategoryView, LovedView, dishAtCaegory, PopularDish, DishSteps, NeironView, FindDish

app_name = "articles"
# app_name will help us do a reverse look-up latter.

urlpatterns = [
    path('dish/<int:pk>', DishView.as_view()),
    path('find/<str:pk>', FindDish.as_view()),
    path('stepsOfDish/<int:pk>', DishSteps.as_view()),
    path('preview/<int:pk>', dishAtCaegory.as_view()),
    path('popular/', PopularDish.as_view()),
    path('preview/', PreviewView.as_view()),
    path('category/', CategoryView.as_view()),
    path('user-dish/<int:pk>', UserDishView.as_view()),
    path('user-dish/loved/', LovedView.as_view()),
    path('neiron', NeironView.as_view()),
]

