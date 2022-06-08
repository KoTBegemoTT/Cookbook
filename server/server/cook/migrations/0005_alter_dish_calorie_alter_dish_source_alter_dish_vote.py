# Generated by Django 4.0 on 2021-12-23 08:14

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('coock', '0004_dish_calorie_dish_category_dish_description_and_more'),
    ]

    operations = [
        migrations.AlterField(
            model_name='dish',
            name='calorie',
            field=models.CharField(max_length=16),
        ),
        migrations.AlterField(
            model_name='dish',
            name='source',
            field=models.CharField(max_length=64),
        ),
        migrations.AlterField(
            model_name='dish',
            name='vote',
            field=models.PositiveIntegerField(),
        ),
    ]
