import os
import keras
from keras.models import Sequential
from tensorflow.keras.layers import Conv2D
from tensorflow.keras.layers  import MaxPooling2D
from tensorflow.keras.layers import Activation
from tensorflow.keras.layers import Flatten, Dropout
from tensorflow.keras.layers import Dense
from keras.datasets import cifar10
from keras.optimizers import RMSprop
from keras.callbacks import TensorBoard, ModelCheckpoint
from tensorflow.keras.preprocessing.image import ImageDataGenerator
import numpy as np




def LeNet(input_shape,num_classes):
    model=Sequential()
    #具体的には、Sequentialモデルは、順番にレイヤーを追加していき、それらの
    #レイヤーを順番に適用するモデルを構築します。
    #layers追加
    #1
    model.add(Conv2D(32,kernel_size=3,padding="same",input_shape=input_shape,activation="relu"))
    model.add(Conv2D(32,kernel_size=3,padding="same",input_shape=input_shape,activation="relu"))
    
    #2
    model.add(MaxPooling2D(pool_size=(2,2)))
    #3
    model.add(Dropout(0.25))
    #4
    model.add(Conv2D(64,kernel_size=3,padding="same",activation="relu"))
    model.add(Conv2D(64,kernel_size=3,padding="same",activation="relu"))
    #5
    #5
    model.add(MaxPooling2D(pool_size=(2,2)))
    #6
    model.add(Flatten())
    #7
    model.add(Dense(512,activation="relu"))
    #8
    model.add(Dropout(0.25))
    #9
    model.add(Dense(num_classes))
    #10
    model.add(Activation("softmax"))
    return model

class CIFAR10Dataset():
    def __init__(self):
        self.image_shape=(32,32,3)
        self.num_classes=10

    def get_batch(self):
        (x_train,y_train),(x_test,y_test)=cifar10.load_data()
        x_train,x_test=[self.preprocess(d) for d in[x_train,x_test]]
        y_train,y_test=[self.preprocess(d,label_data=True) for d in [y_train,y_test]]
        return x_train,y_train,x_test,y_test
    
    def preprocess(self,data ,label_data=False):
        #label_dataか否か
        if label_data:
            data=keras.utils.to_categorical(data,self.num_classes)
        else:
            data=data.astype("float32")
            data/=255
            shape=(data.shape[0],)+self.image_shape
            data=data.reshape(shape)
            
        return data


class Trainer():
    def __init__(self,model,loss,optimizer):
        self._target=model
        self._target.compile(loss=loss,optimizer=optimizer,metrics=["accuracy"])
        self.verbose=1
        self.log_dir=os.path.join(os.path.dirname(__file__),"logdir")
        self.model_file_name="model_file.keras"
    
    def train(self,x_train,y_train,batch_size,epochs,\
              validation_split):
        if os.path.exists(self.log_dir):
            import shutil
            shutil.rmtree(self.log_dir)
        os.mkdir(self.log_dir)
        """
        datagen=ImageDataGenerator(
            featurewise_center=False
            samplewise_center=False
            featurewise_std_normalization=False
            samplewise_std_normalization=False
            zca_whitening=False
            rotation_range=0

        )
        """

        self._target.fit(x_train,y_train,\
                         batch_size=batch_size,epochs=epochs,
                         validation_split=validation_split,\
                            callbacks=[TensorBoard(log_dir=self.log_dir),
                                       ModelCheckpoint(os.path.join(self.log_dir,self.model_file_name),save_best_only=True)],
                                       verbose=self.verbose)
        

dataset=CIFAR10Dataset()
model=LeNet(dataset.image_shape,dataset.num_classes)
x_train,y_train,x_test,y_test=dataset.get_batch()
trainer=Trainer(model,loss="categorical_crossentropy",optimizer=RMSprop())
trainer.train(x_train,y_train,batch_size=128,epochs=12,validation_split=0.2)
score=model.evaluate(x_test,y_test,verbose=0)
print("Test loss:",score[0])
print("Test accuracy:",score[1])
