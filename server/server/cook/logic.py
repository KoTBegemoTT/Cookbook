from django.db.models import Avg

from coock.models import UserDishRelationship, Dish
neironString = "Neiron_unsver"

def set_rating(preview_dish):
    user_dish_relationship = UserDishRelationship.objects.filter(dish=preview_dish)
    rating = user_dish_relationship.aggregate(rating=Avg('rate')).get('rating')
    votes = user_dish_relationship.filter(dish=preview_dish).count()
    if rating is None:
        rating = 0

    preview_dish.rating = rating
    preview_dish.save()

    dish = preview_dish.dish
    dish.rating = rating
    dish.vote = votes
    dish.save()


def getNeironString():
    return neironString

def get_dish_by_ingredients(preview_dish):
    dishes = Dish.objects()
    user_dish_relationship = UserDishRelationship.objects.filter(dish=preview_dish)
