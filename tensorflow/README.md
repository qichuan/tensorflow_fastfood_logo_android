# Instructions

This tutorial will walk you through the whole process from image downloading, model retraining and testing, to Android application integration.

The chosen model for retraining is MobileNet, it is one of the best Neural Network model for image recognition.

## Downlaod the images

Download 300 logo images each for McDonalds, KFC, Burger King and Pizza Hut.

```
python google-images-download.py \
--output_directory "fastfood" \
--keywords "McDonalds Logo" \
--limit 100  \
--format jpg

python google-images-download.py \
--output_directory "fastfood" \
--keywords "KFC Logo" \
--limit 100  \
--format jpg

python google-images-download.py \
--output_directory "fastfood" \
--keywords "Burger King Logo" \
--limit 100  \
--format jpg

python google-images-download.py \
--output_directory "fastfood" \
--keywords "Pizza Hut" \
--limit 100  \
--format jpg

```

Rename the folders

```
mv fastfood/KFC\ Logo/ fastfood/kfc
mv fastfood/Pizza\ Hut\ Logo/ fastfood/pizzahut
mv fastfood/McDonalds\ Logo/ fastfood/mcdonalds
mv fastfood/Burger\ King\ Logo/ fastfood/burgerking

```


## Retrain the model


```
// Create the output folder

mkdir output

IMAGE_SIZE=224
ARCHITECTURE="mobilenet_0.50_${IMAGE_SIZE}"

python retrain.py \
  --image_dir ./fastfood \
  --bottleneck_dir=./output/bottlenecks \
  --how_many_training_steps=500 \
  --model_dir=./output/models/ \
  --summaries_dir=./output/training_summaries/"${ARCHITECTURE}" \
  --architecture="${ARCHITECTURE}" \
  --output_graph ./output/output_graph.pb \
  --output_labels ./output/output_labels.txt \
  --saved_model_dir ./output/saved_model
```

## Test the retrained model
```
python label_image.py \
--graph=./output/output_graph.pb \
--labels=./output/output_labels.txt \
--image=./fastfood/kfc/47.\ kfc-logo.jpg

```

## Optimize model
```
toco \
--input_file=./output/output_graph.pb \
--output_file=./output/optimized_graph.pb \
--input_format=TENSORFLOW_GRAPHDEF \
--output_format=TENSORFLOW_GRAPHDEF \
--input_shape=1,${IMAGE_SIZE},${IMAGE_SIZE},3 \
--input_array=input \
--output_array=final_result
 ```
## Verify the optimized model
```
python label_image.py \
--graph=./output/optimized_graph.pb \
--labels=./output/output_labels.txt \
--image=./fastfood/kfc/2.\ kfc.jpg
```
## Convert model to TFLite format
```
toco \
  --input_file=./output/optimized_graph.pb \
  --output_file=./output/output_graph.lite \
  --input_format=TENSORFLOW_GRAPHDEF \
  --output_format=TFLITE \
  --input_shape=1,${IMAGE_SIZE},${IMAGE_SIZE},3 \
  --input_array=input \
  --output_array=final_result \
  --inference_type=FLOAT \
  --input_type=FLOAT

```
## Integrate to Android app

## References
https://www.tensorflow.org/tutorials/image_retraining