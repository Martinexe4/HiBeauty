# HiBeauty

This repository contains the machine learning model developed for the Capstone Project of Bangkit Academy 2024 Batch 1. The model utilizes TensorFlow and various datasets to detect and classify skin diseases into three categories: acne, eye bags, and oily skin. The model is executed on Google Colab and employs transfer learning using the VGG16 architecture.

## Table of Contents

- [Overview](#overview)
- [Dataset](#dataset)
- [Methodology](#methodology)
- [Model Architecture](#model-architecture)
- [Training and Evaluation](#training-and-evaluation)
- [Model Conversion](#model-conversion)
- [Results](#results)
- [Usage](#usage)
- [Contributors](#contributors)

## Overview

HiBeauty is a deep learning model designed to classify skin conditions from images into three categories: acne, eye bags, and oily skin. The model leverages the pre-trained VGG16 architecture to improve accuracy and efficiency.

## Dataset

The dataset used for training, validation, and testing consists of images classified into three categories:
- Acne
- Eye Bags
- Oily Skin

### Importing Dataset

```python
# Path to the dataset
dataset_path = '/content/gdrive/MyDrive/Capstone Model HiBeauty/Fix Data/Skin_data'
categories = ['acne', 'bags', 'oily']
output_path = './Dataset/Processed_Data'
```

## Methodology

The methodology involves the following steps:
1. **Data Preparation**: Loading and splitting the dataset into training, validation, and testing sets.
2. **Data Augmentation**: Applying transformations to increase dataset diversity.
3. **Model Building**: Using the VGG16 architecture with additional custom layers.
4. **Training**: Training the model with the augmented data.
5. **Evaluation**: Assessing the model's performance on the test set.
6. **Model Conversion**: Converting the trained model to TensorFlow Lite and TensorFlow.js formats for deployment.

## Model Architecture

The model architecture includes:
- VGG16 base model (pre-trained on ImageNet, without top layers)
- Global Average Pooling layer
- Batch Normalization
- Dense layer with ReLU activation
- Dropout layer
- Output Dense layer with softmax activation for three classes

### Model Summary

```python
model.summary()
```

## Training and Evaluation

### Data Generators

```python
train_datagen = ImageDataGenerator(rescale=1./255, shear_range=0.2, zoom_range=0.2, horizontal_flip=True)
val_test_datagen = ImageDataGenerator(rescale=1./255)

train_generator = train_datagen.flow_from_directory(train_dir, target_size=(224, 224), batch_size=32, class_mode='categorical', shuffle=True)
val_generator = val_test_datagen.flow_from_directory(val_dir, target_size=(224, 224), batch_size=32, class_mode='categorical', shuffle=True)
test_generator = val_test_datagen.flow_from_directory(test_dir, target_size=(224, 224), batch_size=32, class_mode='categorical', shuffle=False)
```

### Training the Model

```python
callbacks = [ReduceLROnPlateau(monitor='val_loss', factor=0.2, patience=3, min_lr=0.001), EarlyStopping(monitor='val_loss', patience=5, restore_best_weights=True)]

history = model.fit(train_generator, epochs=20, validation_data=val_generator, callbacks=callbacks)
model.save('HiBeauty_3_disease_model.h5')
```

### Evaluation

```python
test_loss, test_acc = model.evaluate(test_generator)
print(f"Test accuracy: {test_acc}")
```

## Model Conversion

### Convert to TensorFlow Lite

```python
converter = tf.lite.TFLiteConverter.from_keras_model(model)
tflite_model = converter.convert()
with open('HiBeauty_3_disease_model.tflite', 'wb') as f:
    f.write(tflite_model)
```

### Convert to TensorFlow.js

```bash
!tensorflowjs_converter --input_format=keras /content/gdrive/MyDrive/HiBeauty_3_disease_model.h5 /content/gdrive/MyDrive/HiBeauty_3_disease_model
```

## Results

The model's performance is evaluated using accuracy, confusion matrix, and classification report. The final test accuracy is reported along with detailed metrics.

## Usage

To use the model, follow these steps:

1. Clone the repository:
    ```bash
    git clone https://github.com/Martinexe4/HiBeauty.git
    ```

2. Navigate to the project directory:
    ```bash
    cd HiBeauty
    ```

3. Execute the notebook on Google Colab or your local environment to train and evaluate the model.

4. Use the provided script to convert and deploy the model as needed.

## Contributors

- [Muhammad Ridho Mujahid] (https://github.com/Moedjaheed)
- [Martin Ompusunggu] (https://github.com/Martinexe4)
- Yanuar Putra Kharisma Adhiyasa
