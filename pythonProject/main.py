import json
import pandas as pd
from pandas import json_normalize
from pandas import json_normalize
import json
import flask
import requests
from typing import Type
import pandas as pd
import numpy as np
from flask import render_template
import string
import urllib.request
import cv2
from flask import send_file
from flask import jsonify
from sklearn.metrics.pairwise import cosine_similarity
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import  sigmoid_kernel
from sklearn import metrics

import firebase_admin
from firebase_admin import credentials
from firebase_admin import db
cred = credentials.Certificate("my-application-aec78-7121d0776beb.json")
firebase_admin.initialize_app(cred,{
    'databaseURL':'https://my-application-aec78-default-rtdb.firebaseio.com/'
})
# Step 1 Import Python Libraries
# Data processing
import pandas as pd
import numpy as np
import scipy.stats
# Visualization
import seaborn as sns
# Similarity
from sklearn.metrics.pairwise import cosine_similarity
######

import flask
import requests
from flask import render_template
from flask import send_file
from flask import jsonify
#####################################
from typing import Type
import pandas as pd
import numpy as np
import string
from sklearn import metrics
import re
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.metrics import confusion_matrix
from sklearn.model_selection import train_test_split, cross_val_score
from nltk.corpus import stopwords
from nltk.stem import WordNetLemmatizer
from sklearn.svm import SVC
from textblob import TextBlob
from sklearn import svm
import matplotlib.pyplot as plt
from sklearn.neighbors import KNeighborsClassifier

from sklearn.linear_model import LogisticRegression

from sklearn.naive_bayes import GaussianNB

from nltk.sentiment.vader import *
from sklearn.datasets import load_iris
from nltk.sentiment.vader import SentimentIntensityAnalyzer
from nltk import tokenize
import nltk
string.punctuation


ref=db.reference('Rating')
#print(ref.get())
SR=[]
for each in ref.get():
    SR.append(each)
    #print(s)
dfR = pd.DataFrame.from_dict(SR, orient="columns")



ref=db.reference('Movies')
#print(ref.get())
SM=[]
for each in ref.get():
    SM.append(each)
    #print(s)
dfM = pd.DataFrame.from_dict(SM, orient="columns")

print(dfM)

############################# start sentmintal ####################

data = dfR
#data=data.dropna()
data.head()

pd.set_option('display.max_colwidth',None)
data=data[['movieTweets']]
data.head()
print(data)

def Remove_Punctuation(text):
    Punctuation_Free="".join([i for i in text if i not in string.punctuation])
    return Punctuation_Free

data['clean_massage']=data['movieTweets'].apply(lambda x:Remove_Punctuation(x))
data.head()

#print(data['clean_massage'])
#......................................................remove emojis......................................................
def remove_emojis(data):
    emoj = re.compile("["
        u"\U0001F600-\U0001F64F"  # emoticons
        u"\U0001F300-\U0001F5FF"  # symbols & pictographs
        u"\U0001F680-\U0001F6FF"  # transport & map symbols
        u"\U0001F1E0-\U0001F1FF"  # flags (iOS)
        u"\U00002500-\U00002BEF"  # chinese char
        u"\U00002702-\U000027B0"
        u"\U00002702-\U000027B0"
        u"\U000024C2-\U0001F251"
        u"\U0001f926-\U0001f937"
        u"\U00010000-\U0010ffff"
        u"\u2640-\u2642" 
        u"\u2600-\u2B55"
        u"\u200d"
        u"\u23cf"
        u"\u23e9"
        u"\u231a"
        u"\ufe0f"  # dingbats
        u"\u3030"
                      "]+", re.UNICODE)
    return re.sub(emoj, '', data)

data['without emojis']=data['clean_massage'].apply(lambda x:remove_emojis(x))
#print(data['without emojis'])
#data Lower case
data['massage_lower']= data['without emojis'].apply(lambda x: x.lower())

#Tokenization(spliting massage into parts)
def Tokenization(text):
    take_text=re.split(' ',text)
    return take_text
data['Tokenization_massage']=data['massage_lower'].apply(lambda x:Tokenization(x))
#print(data['Tokenization_massage'])

#Stop word removal(btshel el kalam ale mlosh lzma zy i w my and so on
#defining the function to remove stopwords from tokenized text
StopWords =set(stopwords.words('english'))

#.......................remove empty string.................................................
def remove_empty_strings(text):
    without_empty_strings = [string for string in text if string != ""]
    return without_empty_strings
data['without_empty_strings']=data['Tokenization_massage'].apply(lambda x:remove_empty_strings(x))
#print(data['without_empty_strings'])

#.......................Remove StopWords string.................................................
def Remove_StopWords(text):
    OutPut_After_Remove= [i for i in text if not i.lower() in StopWords]
    return OutPut_After_Remove
data['No_StopWord']=data['without_empty_strings'].apply(lambda x:Remove_StopWords(x))
#print(data['No_StopWord'])

#Lemmatization:(byshof elklma lw atshal enha 7agat keda hykon leha m3na wala la lw mlhash hysbha nfs el shkl
Wordnet_Lemmatizer = WordNetLemmatizer()
def Lemmatizer(text):
    Lemmatizer_Text = [Wordnet_Lemmatizer.lemmatize(word) for word in text]
    return Lemmatizer_Text
data['massage_lemmatized']=data['No_StopWord'].apply(lambda x:Lemmatizer(x))
#print(data['massage_lemmatized'])

data['massage_lemmatized']=data['massage_lemmatized'].astype('str')
#print(data['massage_lemmatized'])
def get_polarity(text):
    return TextBlob(text).polarity
data['Polarity']=data['massage_lemmatized'].apply(get_polarity)
print("dattaaaaaaa ppooollaaaariityyyy")
print(data['Polarity'])

ref2=db.reference('Rating')
data2=data['Polarity']*10
z=0
for i in data2:
    x=str(z)
    #eftkr t3ml rounding
    ref2.child(x).update({"polarity_Score":round(i,2)})
    z+=1


#identify Sentmental Type
data['Sentmental_type']=''
data.loc[data.Polarity > 0,'Sentmental_type']='Positive'
data.loc[data.Polarity == 0,'Sentmental_type']='Neutral'
data.loc[data.Polarity < 0,'Sentmental_type']=' Negative'
print(data['Sentmental_type'])
print('***************************************')
#print(data['massage_lemmatized'])

#.......................Split data into train and test data .................................................
np.random.seed(2)
#df1 = data.sample(frac = 0.3)
#data['Sentmental_type']=data.Polarity.apply(lambda x: 0 if x in [1, 2] else 1)
X=data['massage_lemmatized']
Y=data['Sentmental_type']

#---------------split-------------
X_train, X_test, Y_train, Y_test = train_test_split(X,Y, test_size = 0.5,shuffle=False)
#Vectorizing the text data (bt7wlha l vector)
cv = CountVectorizer()
ctmTr = cv.fit_transform(X_train)
X_test_dtm = cv.transform(X_test)
                    #---------------------SVM---------------------
#--------------------Training Model(using algorithm)-----------
#svc==>support vector classifcation

#print('**SVM**')
#svcl = svm.SVC()
#svcl.fit(ctmTr,Y_train)
#svcl_score=svcl.score(X_test_dtm,Y_test)
#print(svcl_score)
#print(svcl_score)==> 0.50
#y_pred_sv = svcl.predict(X_test_dtm)
#print(y_pred_sv)
#print('******************************************************')
#print(Y_test)
#Confusion matrix
#ravel()==> lw 3nde kza array aw array 2D yb7tohom f array wa7d by3ml concatenate

#conf_mat = confusion_matrix(Y_test,y_pred_sv)
#print(conf_mat)
#AccuracyS = (sum(conf_mat.diagonal())) / np.sum(conf_mat)
#print("Accuracy:", svcl_score)
#print('******************************************************')

                #--------------------KNN----------------------------
#knn = KNeighborsClassifier(n_neighbors=5)
#knn.fit(ctmTr, Y_train)
#knn_score = knn.score(X_test_dtm, Y_test)
#print("Results for KNN Classifier with CountVectorizer")

#print(knn_score)
#print('******************************************************')
#y_pred_knn = knn.predict(X_test_dtm)
#Confusion matrix
#cm_knn = confusion_matrix(Y_test, y_pred_knn)
#AccuracyK = (sum(cm_knn.diagonal())) / np.sum(cm_knn)
#print("Accuracy:", AccuracyK*100)

                #-------------------------Logistic Regression---------------------


# lr = LogisticRegression()
# lr.fit(ctmTr, Y_train)
# #Accuracy score
# lr_score = lr.score(X_test_dtm, Y_test)
# #print("Results for Logistic Regression with CountVectorizer")
# #print(lr_score)
# #Predicting the labels for test data
# #print('******************************************************')
# y_pred_lr = lr.predict(X_test_dtm)
# #print(y_pred_lr)
# #print('******************************************************')
# #Confusion matrix
# cm_lr = confusion_matrix(Y_test, y_pred_lr)
# #print(cm_lr)
# #print('******************************************************')
# AccuracyL = (sum(cm_lr.diagonal())) / np.sum(cm_lr)
                #----------------------Naive Bayes------------------------
#ytrain=np.array(Y_train)
#xtrain=ctmTr.toarray()
#ytest=np.array(Y_test)
#xtest=X_test_dtm.toarray()
#gnb = GaussianNB()
#gnb.fit(xtrain, ytrain)

#GNB_score = gnb.score(xtest, ytest)
#print("Results for GaussianNB with CountVectorizer")
#print(GNB_score)
#print('******************************************************')
# making predictions on the testing set
#y_pred_NB = gnb.predict(xtest)
#print(y_pred_NB)
#print('******************************************************')
#Confusion matrix
#cm_NB = confusion_matrix(ytest, y_pred_NB)
#print(cm_NB)
#print('******************************************************')
#AccuracyNB = (sum(cm_NB.diagonal())) / np.sum(cm_NB)
#print("Accuracy:", AccuracyNB*100)
#print('******************************************************')
#print('******************************************************')
# print("LR Accuracy:", lr_score*100)
#print("SVM Accuracy:", svcl_score*100)
#print("KNN Accuracy:", knn_score*100)
#print("GNB Accuracy:", GNB_score*100)
#--------------------------------------------


############################# end sentmintal ####################


############################# start Collaborative ####################


#print(dfM)
# Read in data
ratings = dfR
# Take a look at the data
ratings.head()
# Get the dataset information
print(ratings)

# Read in data
movies = dfM
# Take a look at the data
movies.head()
print(movies)
# Merge ratings and movies datasets
df = pd.merge(ratings, movies, on='movieId', how='inner')
# Take a look at the data
df.head()

###### Step 3 Exploratory Data Analysis (EDA)
# Aggregate by movie
agg_ratings = df.groupby('title').agg(mean_rating=('polarity_Score', 'mean'),
                                      number_of_ratings=('polarity_Score', 'count')).reset_index()
# Keep the movies with over 100 ratings
agg_ratings_GT100 = agg_ratings[agg_ratings['number_of_ratings'] > 2]
agg_ratings_GT100.info()
# Check popular movies
agg_ratings_GT100.sort_values(by='number_of_ratings', ascending=False).head()
# Visulization
sns.jointplot(x='mean_rating', y='number_of_ratings', data=agg_ratings_GT100)
# Merge data
df_GT100 = pd.merge(df, agg_ratings_GT100[['title']], on='title', how='inner')
df_GT100.info()

###### Step 4 Create User-Movie Matrix
# Create user-item matrix
matrix = df_GT100.pivot_table(index='userId', columns='title', values='polarity_Score')

matrix.head()
###### Step 5 Data Normalization
# Normalize user-item matrix
matrix_norm = matrix.subtract(matrix.mean(axis=1), axis='rows')
matrix_norm.head()
###### Step 6 Identify Similar Users
# User similarity matrix using Pearson correlation
user_similarity = matrix_norm.T.corr()
user_similarity.head()
# User similarity matrix using cosine similarity
user_similarity_cosine = cosine_similarity(matrix_norm.fillna(0))
user_similarity_cosine
# Number of similar users
n = 6
# User similarity threashold
user_similarity_threshold = 0.3

picked_userid = 1

# Get top n similar users
similar_users = user_similarity[user_similarity[picked_userid] > user_similarity_threshold][picked_userid].sort_values(
    ascending=False)[:n]
# Print out top n similar users
print(f'The similar users for user {picked_userid} are', similar_users)
###### Step 7 Narrow Down Item Pool
# Remove movies that have been watched
picked_userid_watched = matrix_norm[matrix_norm.index == picked_userid].dropna(axis=1, how='all')
picked_userid_watched
# Movies that similar users watched. Remove movies that none of the similar users have watched
similar_user_movies = matrix_norm[matrix_norm.index.isin(similar_users.index)].dropna(axis=1, how='all')
similar_user_movies
# Remove the watched movie from the movie list
similar_user_movies.drop(picked_userid_watched.columns, axis=1, inplace=True, errors='ignore')
# Take a look at the data
similar_user_movies.columns
print(similar_user_movies.columns)

###### Step 8 Recommend Items
# A dictionary to store item scores

############################# end Collaborative ####################


############################# start content ####################


data_set=dfM

data_set=data_set[['id','title','overview','genres','Movie_picture']]
data_set.head()
#print(data_set['Movie_picture'])
#tfidf matrix gets a tuple docno,feature
#ya3ny maslan doc 0 feh el kelma de bnsbt ad ehh we hakaza
tfidf = TfidfVectorizer(stop_words='english')
data_set['overview'] = data_set['overview'].fillna('')

tfidf_matrix = tfidf.fit_transform(data_set['overview'])
tfidf_matrix.shape
#print(tfidf_matrix)
#for i, feature in enumerate(tfidf.get_feature_names()):
   # print(i, feature)
#compute the sigmoid kernel
#simmilarity between two movies (percentage between 0 and 1)sig[movie1,movie2]
sig = sigmoid_kernel(tfidf_matrix,tfidf_matrix)
print(sig[0])
#getting movie title with index
indices = pd.Series(data_set.index,index=data_set['title']).drop_duplicates()
#print(indices)

############################# end content ####################


############# Start Server ####################
app = flask.Flask(__name__)

# to display the connection status
@app.route('/', methods=['GET'])
def handle_call():
    return "Successfully Connected"

@app.route('/simimov2/<ID>', methods=['GET'])
def recommendcolla(ID):
    picked_userid = int(ID)
    similar_users = user_similarity[user_similarity[picked_userid] > user_similarity_threshold][
                        picked_userid].sort_values(
        ascending=False)[:n]
    picked_userid_watched = matrix_norm[matrix_norm.index == picked_userid].dropna(axis=1, how='all')
    similar_user_movies = matrix_norm[matrix_norm.index.isin(similar_users.index)].dropna(axis=1, how='all')
    similar_user_movies.drop(picked_userid_watched.columns, axis=1, inplace=True, errors='ignore')
    names2 = ""


    dataframeM=pd.DataFrame({'title':movies['title'] ,'Movie_picture':movies['Movie_picture'] })

    for i in similar_user_movies.columns:

        urls=dataframeM[dataframeM['title']==i]
        names2 += str(i) + "splitArray" + str(urls['Movie_picture'].item()) + "splitArray"

    return names2


#the post method. when we call this with a string containing a name, it will return the name with the text "I got your name"
@app.route('/getname/<title>', methods=['GET'])
def recommendconta(title,sig=sig):
    #get index of enterd movie
    idx=indices[title]
    #get the simmilarity of the movie with other movies using sigmoid and put it in a list
    sig_scores = list(enumerate(sig[idx]))
    #sort movies
    sig_scores=sorted(sig_scores,key=lambda x: x[1],reverse=True)
    #get the scores of top most 10
    sig_scores=sig_scores[1:11]
    #movie indices
    movie_indices=[i[0] for i in sig_scores]
    #top 10 simmilar movies
    names = ""
    for i in movie_indices:
        #names.append(data_set['title'].iloc[i])
        url=data_set['Movie_picture'].iloc[i]
        #print(url)
       #url_response = urllib.request.urlopen(url)
        #img_array = np.array(bytearray(url_response.read()),dtype=np.uint8)
        #img = cv2.imdecode(img_array,1)
        #cv2.imshow('movie pic', img)
        #cv2.waitKey(1)
        #names+=data_set['title'].iloc[i]+","+data_set['Movie_picture']
        names+=data_set['title'].iloc[i]+"splitArray"+data_set['Movie_picture'].iloc[i]+"splitArray"
        print(names)
    return names


# this commands the script to run in the given port
if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)
############# end Server ####################



