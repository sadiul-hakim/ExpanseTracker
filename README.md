# SSL
SSL certification (commonly referred to as an SSL certificate) is a digital certificate that authenticates a website's identity and enables an encrypted connection between a web server and a web browser. SSL stands for Secure Sockets Layer, which is the predecessor to the more modern TLS (Transport Layer Security)—but the term "SSL" is still widely used.

## 🔐 What Does an SSL Certificate Do?
1. Encrypts data:
- It ensures that all data transferred between the user and the website is encrypted (like passwords, credit card numbers, etc.).
- Prevents "man-in-the-middle" attacks where attackers can snoop or tamper with data.
2. Authenticates identity:
- Confirms that the website is legitimate and owned by the organization it claims to be.
- Helps protect users from phishing sites.
3. Enables HTTPS:
- You know that little lock icon 🔒 in your browser’s address bar? That’s SSL in action.
- Without an SSL certificate, browsers will show a "Not Secure" warning for websites using plain HTTP.

## 🛠️ Why Do We Need SSL Certificates?
1. 	Encrypts sensitive info like login credentials, personal data, payment info.
2. Visitors feel safer when they see HTTPS and the lock icon.
3. 	Google gives ranking advantages to HTTPS sites.
4. Many data protection regulations (like GDPR, PCI-DSS) require encryption.
5. 	Modern browsers warn users when accessing non-HTTPS websites.

# Stablishe SSL Secure connection in Spring Boot App

## Put configurations
```
# SSL
server.ssl.key-alias=expanseTrackerApi-https
server.ssl.key-store=classpath:expanseTrackerApi.jks
server.ssl.key-store-type=JKS
server.ssl.key-password=#(expanseTrackerApi)-https123
```

## Generate the certificate from cli
```
keytool -genkey -alias expanseTrackerApi-https -storetype JKS -keyalg RSA -keysize 2048 -validity 365 -keystore expanseTrackerApi.jks
```

## Get the file from drive and put under resources