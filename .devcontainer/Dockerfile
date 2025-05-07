
FROM mcr.microsoft.com/vscode/devcontainers/android:stable

   # Install Flutter
   RUN git clone https://github.com/flutter/flutter.git /usr/local/flutter && \
       export PATH="/usr/local/flutter/bin:$PATH" && \
       flutter precache && \
       flutter doctor
