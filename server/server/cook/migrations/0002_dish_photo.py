# Generated by Django 4.0 on 2021-12-20 16:16

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('coock', '0001_initial'),
    ]

    operations = [
        migrations.AddField(
            model_name='dish',
            name='photo',
            field=models.ImageField(blank=True, upload_to='photos'),
        ),
    ]
