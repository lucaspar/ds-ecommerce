Distributed e-commerce
===

#### An online shopping distributed architecture proposal using tuple space, message queuing, and web services.

## Running
> Build files required

```bash
# SETTING

cd build
# set aws credentials
mv .aws ~

# set web server
unzip apache-tomcat-8.0.45.zip
mv sun-jaxws.xml apache-tomcat-8.0.45/lib/
cd artifacts
cp ws_ecommerce/ws_ecommerce.war ../apache-tomcat-8.0.45/webapps/


# EXECUTING

# run manager
java -jar manager_jar/ecommerce.jar

# run supplier
java -jar supplier_jar/ecommerce.jar 192.168.x.x

# run web server
cd ../apache-tomcat-8.0.45/bin
sh catalina.sh run

# run client
cd client
sensible-browser index.html

```

## Troubleshooting

* Supplier cannot connect to manager showing `Connection refused to host: 127.0.1.1`:

	1. Double check the IP in argument: `java -jar supplier_jar/ecommerce.jar 192.168.x.x`

	2. In manager's machine `/etc/hosts`, add the local IP:

        Replace `127.0.1.1 <hostname>` with `192.168.x.x <hostname>`
