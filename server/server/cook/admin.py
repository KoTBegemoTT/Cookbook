from django.contrib import admin

# Register your models here.
from django.contrib.admin import ModelAdmin

from coock.models import *


@admin.register(Dish)
class DishAdmin(ModelAdmin):
    pass


@admin.register(Step)
class DishAdmin(ModelAdmin):
    pass


@admin.register(Categorie)
class CategorieAdmin(ModelAdmin):
    pass


@admin.register(Preview)
class PreviewAdmin(ModelAdmin):
    pass


@admin.register(UserDishRelationship)
class UDRAdmin(ModelAdmin):
    pass
