## Setup
1.  In root build.gradle:
  ```
  allprojects {
    repositories {
      ...
      maven { url 'https://jitpack.io' }
    }
  }
  ````

2.  In target module build.gradle
  ```
  dependencies {
    compile 'com.github.potterhsu:Pinger:v1.0'
  }
  ```

## Usage
1. Ping directly in synchronization:
  ```java
  Pinger pinger = new Pinger();
  pinger.ping("8.8.8.8", 3);
  ```

2. Ping in asynchronization until it is succeeded:
  ```java
  Pinger pinger = new Pinger();
  pinger.setOnPingListener(new Pinger.OnPingListener() {
      @Override
      public void onPingSuccess() { ... }

      @Override
      public void onPingFailure() { ... }

      @Override
      public void onPingFinish() { ... }
  });
  pinger.pingUntilSucceeded("8.8.8.8", 5000);
  ```

3. Ping in asynchronization until it is failed:
  ```java
  Pinger pinger = new Pinger();
  pinger.setOnPingListener(new Pinger.OnPingListener() {
      @Override
      public void onPingSuccess() { ... }

      @Override
      public void onPingFailure() { ... }

      @Override
      public void onPingFinish() { ... }
  });
  pinger.pingUntilFailed("8.8.8.8", 5000);
  ```
