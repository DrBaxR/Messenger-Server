# Setup needed before running
First off, you need to get this project on your machine. You can either choose to download and extract a ZIP containing it or you can clone this repository by running the following command after navigating to the directory where you want the project to be in (in case you don't have **git** installed, you can get it from [here](https://git-scm.com/)):

```git clone https://github.com/DrBaxR/Messenger-Server.git```

**Clone from here if you only want to get the project and not contribute** (more details in the _contributing_ section).


After doing that, you will need to install [MongoDB](https://www.mongodb.com/try/download/community). Also install _MongoDB Compass_ if you want to be able to view the data you added to your database in a graphical interface.

Now you have everything you need to run the project. Nice! The first thing you will need to do is have the **MongoDB** server running locally. How do you do that? Well, you just have to navigate in a terminal to the directory where you installed it (for example '_C:\Program Files\MongoDB\Server\4.4\bin_') and run the ```mongod``` command. **Leave this terminal running while the server is running**.

After doing all that, you can finally start the server by navigating to the root folder of the project - the one containing the _'src/'_ folder - and running the following command: ```mvnw spring-boot:run``` for Windows or ```./mvnw spring-boot:run``` for Mac and Linux.

Alternatively, if you have [Maven](https://maven.apache.org/download.cgi) installed on your machine, you can just run ```mvn spring-boot:run```.

**From version v1.3.0: ** You must also create two environment variables (```MESSENGER_SERVER_EMAIL``` and ```MESSENGER_EMAIL_PASSWORD```). These two variables represent a gmail account from which the server will send emails to users that forgot their password. In case you run into any problems with the credentials, please read [this](https://support.google.com/mail/answer/7126229?visit_id=637540027062990845-726287386&rd=1) and make sure you follow the steps described.

# Contributing
In case you want to contribute to the development of this project, you will first need to fork this repository by clicking on the _Fork_ button that appears in the top right corner of this page. 

After doing that, you should have created a version of this repo in your Github account. Sweet! Now you can clone it (the version you created) so that you have access to it locally, on your machine. The clone command should look something like this:

```git clone https://github.com/[your-username]/Messenger-Server.git```

When you've done that, you can choose to sync your fork with the main repo, by using:

```git remote add upstream https://github.com/DrBaxR/Messenger-Server.git```

You can read more (and I recommend you do, in case you are not familiar) about forks in the [Working with forks](https://docs.github.com/en/github/collaborating-with-issues-and-pull-requests/working-with-forks) section of the **GitHub** documentation. Also, [here](https://www.dataschool.io/how-to-contribute-on-github/) is a pretty cool tutorial about forks I found.

This being done, you can finally start working on the project, make commits locally and push them to your fork of the repo and after you are done, you just need to create a [Pull Request](https://docs.github.com/en/github/collaborating-with-issues-and-pull-requests/creating-a-pull-request), I'll have a look at what you've done and if everything looks good, _I'll merge it into the **main repository**_.
