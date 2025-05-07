# Use a base image with Flutter pre-installed
FROM cirrusci/flutter:latest

# Install Android SDK dependencies
RUN apt-get update && apt-get install -y --no-install-recommends \
    lib32stdc++6 \
    lib32z1 \
    openjdk-11-jdk \
    unzip \
    wget \
    && rm -rf /var/lib/apt/lists/*

# Set Android SDK environment variables
ENV ANDROID_HOME=/opt/android-sdk
ENV PATH=$ANDROID_HOME/emulator:$ANDROID_HOME/tools:$ANDROID_HOME/tools/bin:$ANDROID_HOME/platform-tools:$PATH

# Install Android SDK
RUN mkdir -p $ANDROID_HOME && \
    wget https://dl.google.com/android/repository/commandlinetools-linux-8512546_latest.zip -O sdk-tools.zip && \
    unzip sdk-tools.zip -d $ANDROID_HOME/cmdline-tools && \
    rm sdk-tools.zip && \
    yes | sdkmanager --licenses && \
    sdkmanager "platform-tools" "platforms;android-32" "build-tools;32.0.0"

# Create a non-root user and set permissions
ARG USERNAME=vscode
ARG USER_UID=1000
ARG USER_GID=$USER_UID

RUN groupadd --gid $USER_GID $USERNAME \
    && useradd --uid $USER_UID --gid $USER_GID -m $USERNAME \
    && apt-get install -y sudo \
    && echo $USERNAME ALL=\(ALL\) NOPASSWD:ALL > /etc/sudoers.d/$USERNAME \
    && chmod 0440 /etc/sudoers.d/$USERNAME

# Switch to non-root user
USER $USERNAME
WORKDIR /workspace

# Pre-cache Flutter dependencies
RUN flutter precache && \
    flutter config --enable-web && \
    flutter doctor
