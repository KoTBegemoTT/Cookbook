import torch
import torch.nn as nn
from django.core.files.storage import FileSystemStorage
from torchvision import transforms, models
from collections import defaultdict
import matplotlib.pyplot as plt
from PIL import Image

device = torch.device('cpu')
import json

en2ru = {}
with open('C:/Users/1135532/PycharmProjects/test_coock5/server/coock/recipe2ingredient/clean_ingred.txt', 'r') as f:
    en = f.readlines()
with open('C:/Users/1135532/PycharmProjects/test_coock5/server/coock/recipe2ingredient/rus_clean_ingred.txt', 'r', encoding='utf-8') as f:
    ru = f.readlines()

for e, r in zip(en, ru):
    en2ru[e.rstrip('\n')] = r.rstrip('\n')
with open('C:/Users/1135532/PycharmProjects/test_coock5/server/coock/recipe2ingredient/en2ru_ing.json', 'w') as f:
    json.dump(en2ru, f)

with open('C:/Users/1135532/PycharmProjects/test_coock5/server/coock/recipe2ingredient/clean_ingred.txt', 'r') as f:
    clean_ingredients = list(f.read().split('\n'))
    print(len(clean_ingredients))

id2word = defaultdict()
for i, ingr in enumerate(clean_ingredients):
    id2word[i + 1] = ingr

rus_id2word = defaultdict()
for i, ingr in enumerate(clean_ingredients):
    rus_id2word[i + 1] = ingr

word2id = {v: k for k, v in id2word.items()}


def words2ids(ingreds):
    return [word2id[ing] for ing in ingreds]


model = models.densenet161(pretrained=True)


def init_model():
    for param in model.parameters():
        param.requires_grad = False
    num_feat = model.classifier.in_features

    model.classifier = nn.Sequential(
        nn.Linear(num_feat, 1024),
        nn.BatchNorm1d(1024),
        nn.ReLU(),
        nn.Linear(1024, 512),
        nn.BatchNorm1d(512),
        nn.ReLU(),
        nn.Linear(512, len(word2id)),
        nn.Sigmoid())
    model.to(device)

    state_dict = torch.load("C:/Users/1135532/PycharmProjects/test_coock5/server/coock/recipe2ingredient/model_encoder_classifier_best.pth",
                            map_location=torch.device('cpu'))
    listParams = list()
    for param_tensor in state_dict:
        if "classifier.4" in param_tensor:
            listParams.append(param_tensor)
    for param in listParams:
        newParam = param.replace("classifier.4", "classifier.3")
        state_dict[newParam] = state_dict.pop(param)
    listParams = list()
    for param_tensor in state_dict:
        if "classifier.6" in param_tensor:
            listParams.append(param_tensor)
    for param in listParams:
        newParam = param.replace("classifier.6", "classifier.4")
        state_dict[newParam] = state_dict.pop(param)
    listParams = list()
    for param_tensor in state_dict:
        if "classifier.8" in param_tensor:
            listParams.append(param_tensor)
    for param in listParams:
        newParam = param.replace("classifier.8", "classifier.6")
        state_dict[newParam] = state_dict.pop(param)

    model.load_state_dict(state_dict)
    model.eval()



def predict_image(path_to_img, file_name, transform):
    with torch.no_grad():
        img = Image.open(path_to_img).convert('RGB')

    fs = FileSystemStorage('media\\neiron')

    fs.delete(file_name)

    img_dev = transform(img).to(device).unsqueeze(0)

    ingred_pred = model(img_dev) > 0.25
    ingred_pred = ingred_pred.nonzero()[:, 1].tolist()
    ingred_pred = [id2word[ing + 1] for ing in ingred_pred]
    ingred_pred = [en2ru[ing] for ing in ingred_pred]


    result = '&&'.join(ingred_pred)
    # res_list = result.replace('\t', '').split('\n')
    # print (res_list)
    # plt.figure(figsize=(12, 8))
    # plt.imshow(img)
    # plt.axis('off')
    # plt.show()
    return result


def predict_image_without_path(img):
    # with torch.no_grad():
    #     img = Image.open(path_to_img).convert('RGB')
    # img.convert('RGB')
    img_dev = transform(img).to(device).unsqueeze(0)

    ingred_pred = model(img_dev) > 0.25
    ingred_pred = ingred_pred.nonzero()[:, 1].tolist()
    ingred_pred = [id2word[ing + 1] for ing in ingred_pred]
    ingred_pred = [en2ru[ing] for ing in ingred_pred]


    result = '&&'.join(ingred_pred)
    # res_list = result.replace('\t', '').split('\n')
    # print (res_list)
    # plt.figure(figsize=(12, 8))
    # plt.imshow(img)
    # plt.axis('off')
    # plt.show()
    return result


init_model()

transform = transforms.Compose([
    transforms.Resize((250, 250)),
    transforms.CenterCrop((224, 224)),
    transforms.ToTensor(),
    transforms.Normalize(mean=(0.485, 0.456, 0.406), std=(0.229, 0.224, 0.225))
])
# path_image = "C:/Users/1135532/PycharmProjects/recipe2ingredient/media/dish_1.jpg"

def getPrediction(path_image, file_name):
    return predict_image(path_image, file_name, transform)


# for i in range(16, 17):
#     path_image = "C:/Users/1135532/PycharmProjects/recipe2ingredient/media/dish_" + i.__str__() + ".jpg"
#     predict_image(path_image, transform)


