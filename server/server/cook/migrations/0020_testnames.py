# Generated by Django 4.0 on 2022-01-09 15:45

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('coock', '0019_delete_article_delete_author'),
    ]

    operations = [
        migrations.CreateModel(
            name='TestNames',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('name', models.CharField(max_length=255, verbose_name='имя')),
            ],
        ),
    ]
