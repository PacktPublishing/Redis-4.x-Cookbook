
#!/usr/bin/env python
import datetime
import os
import smtplib
import sys
from email.mime.multipart import MIMEMultipart
from email.mime.application import MIMEApplication
from email.mime.base import MIMEBase
from email.mime.text import MIMEText
from email.utils import formatdate
from email import Encoders
from email.message import Message

def main():
    event_type = sys.argv[1]
    event_desc = sys.argv[2]
    mail_content = event_type + ":" + event_desc

    send_mail("redis-sentinel-notify@example.com",
              ["redis-ops@example.com","redis-notify-group@example.com"],
              "Redis Sentinel Event Notification Mail",
              mail_content,
              cc=["xxx@example.com"],
              bcc=["xxx@example.com"]
            )
    sys.exit(0)

def send_mail(fromPerson,toPerson, subject="", text="",files=[], cc=[], bcc=[]):
    server = "smtp.example.com"
    message = MIMEMultipart()
    message['From'] = fromPerson
    message['To'] = ', '.join(toPerson)
    message['Date'] = formatdate(localtime=True)
    message['Subject'] = subject
    message['Cc'] = ','.join(cc)
    message['Bcc'] = ','.join(bcc)
    message.attach(MIMEText(text))

    for f in files:
        part = MIMEApplication(open(f,"rb").read())
        part.add_header('Content-Disposition', 'attachment', filename=filename)
        message.attach(part)

    addresses = []
    for x in toPerson:
        addresses.append(x)
    for x in cc:
        addresses.append(x)
    for x in bcc:
        addresses.append(x)

    smtp = smtplib.SMTP_SSL(server)
    smtp.login("redis-sentinel-notify","xxxxxx")
    smtp.sendmail(message['From'],addresses,message.as_string())
    smtp.close()
    
if __name__ == '__main__':
main()
