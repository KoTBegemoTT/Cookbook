# Generated by Django 4.0 on 2021-12-24 06:05

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('coock', '0011_alter_userdishrelationship_rate'),
    ]

    operations = [
        migrations.AlterField(
            model_name='userdishrelationship',
            name='rate',
            field=models.PositiveSmallIntegerField(null=True),
        ),
    ]
