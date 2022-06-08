from django.db import models
from django.contrib.auth.models import User


class Categorie(models.Model):
    name = models.CharField("Название", max_length=255)
    web_image = models.ImageField("Image for web", upload_to='category\web', blank=True)
    mobile_image = models.ImageField("Image for mobile", upload_to='category\mobile', blank=True)

    def __str__(self):
        return self.name


class NeironImage(models.Model):
    web_image = models.ImageField("Image for web", upload_to='neiron', blank=True)



class Dish(models.Model):
    name = models.CharField(max_length=255)
    calorie = models.CharField(max_length=16, default="")
    source = models.CharField(max_length=64)
    web_image = models.ImageField("Image for web", upload_to='dish\web', blank=True)
    mobile_image = models.ImageField("Image for mobile", upload_to='dish\mobile', blank=True)
    vote = models.PositiveIntegerField(default=0)
    description = models.TextField()
    ingredients_name = models.TextField()
    ingredients_number = models.TextField()
    category = models.ForeignKey(Categorie, on_delete=models.SET_NULL, null=True)
    recipe = models.TextField()
    rating = models.DecimalField(max_digits=3, decimal_places=2, default=0)

    def __str__(self):
        return self.name


class Step(models.Model):
    number = models.PositiveSmallIntegerField(default=0)
    description = models.TextField()
    image = models.ImageField("Step illustration", upload_to='dish\steps', blank=True)
    dish = models.ForeignKey(Dish, on_delete=models.CASCADE)

    def __str__(self):
        return self.dish.name + ": " + self.number.__str__()


class Preview(models.Model):
    name = models.CharField("Название", max_length=255)
    short_description = models.TextField(null=True)
    web_image = models.ImageField("Image for web", upload_to='preview\web', blank=True)
    mobile_image = models.ImageField("Image for mobile", upload_to='preview\mobile', blank=True)
    ingredients = models.PositiveSmallIntegerField(default=0)
    steps = models.PositiveSmallIntegerField(default=0)
    dish = models.ForeignKey(Dish, on_delete=models.CASCADE)
    category = models.ForeignKey(Categorie, on_delete=models.SET_NULL, null=True)
    rating = models.DecimalField(max_digits=3, decimal_places=2, default=0)

    def __str__(self):
        return self.name


class UserDishRelationship(models.Model):
    user = models.ForeignKey(User, on_delete=models.CASCADE)
    dish = models.ForeignKey(Preview, on_delete=models.CASCADE)
    rate = models.PositiveSmallIntegerField(null=True, blank=True)
    loved = models.BooleanField(default=False)

    def save(self, *args, **kwargs):
        from coock.logic import set_rating

        creating = not self.pk
        old_rating = self.rate

        super().save(*args, **kwargs)

        new_rating = self.rate

        if old_rating != new_rating or creating:
            set_rating(self.dish)

    class Meta:
        unique_together = ('user', 'dish')
