FROM eclipse-temurin:17-jre-alpine

WORKDIR /home

COPY target/carl-bot-1.0.0-jar-with-dependencies.jar ./
COPY data data

ARG DB_ROOT_PASSWORD
ARG DISCORD_BOT_TOKEN
ARG FLICKR_API_KEY
ARG FLICKR_SHARED_SECRET
ARG SPOTIFY_CLIENT_ID
ARG SPOTIFY_CLIENT_SECRET
RUN echo //db:3306/cards > database.ini && \
    echo root >> database.ini && \
    echo $DB_ROOT_PASSWORD >> database.ini && \
    echo $DISCORD_BOT_TOKEN > discord.ini && \
    echo $FLICKR_API_KEY > flickr.ini && \
    echo $FLICKR_SHARED_SECRET >> flickr.ini && \
    echo $SPOTIFY_CLIENT_ID > spotify.ini && \
    echo $SPOTIFY_CLIENT_SECRET >> spotify.ini

ENTRYPOINT ["java", "-jar", "carl-bot-1.0.0-jar-with-dependencies.jar"]