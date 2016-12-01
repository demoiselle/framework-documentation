# Utilização do Módulo
Passos para utilizaçao do módulo...


Add no pom.xml

```xml
<dependency>
    <groupId>org.demoiselle.jee</groupId>
    <artifactId>demoiselle-multitenancy-hibernate</artifactId>
</dependency>
```

Add no demoiselle.properties

```
# SQL
demoiselle.multiTenancyMasterDatabase=user_master
demoiselle.multiTenancySetDatabaseSQL=USE
demoiselle.multiTenancyCreateDatabaseSQL=CREATE DATABASE IF NOT EXISTS
demoiselle.multiTenancyDropDatabaseSQL=DROP DATABASE IF EXISTS
demoiselle.multiTenancyTenantDatabasePrefix=user_

# Data source used for Hibernate
demoiselle.multiTenancyTenantsDatabaseDatasource=java:jboss/datasources/UserTenantsDS
demoiselle.multiTenancyMasterDatabaseDatasource=java:jboss/datasources/UserMasterDS

# DDL Files Path
demoiselle.multiTenancyCreateDatabaseDDL=../standalone/tmp/demoiselleMultiTenantCreate_User.ddl
demoiselle.multiTenancyDropDatabaseDDL=../standalone/tmp/demoiselleMultiTenantDrop_User.ddl
```

Add no persistence.xml

```xml
<persistence-unit name="UserTenantsPU" transaction-type="JTA">
    <provider>org.hibernate.ejb.HibernatePersistence</provider>
    <jta-data-source>java:jboss/datasources/UserTenantsDS</jta-data-source>
    <class>org.demoiselle.jee7.example.store.user.entity.User</class>
    <!-- <exclude-unlisted-classes>true</exclude-unlisted-classes> -->
    <properties>
        <property name="hibernate.dialect" value="org.hibernate.dialect.MySQLDialect" />
        <property name="hibernate.multiTenancy" value="SCHEMA" />
        <property name="hibernate.tenant_identifier_resolver"
            value="org.demoiselle.jee.multitenancy.hibernate.dao.SchemaResolver" />
        <property name="hibernate.multi_tenant_connection_provider"
            value="org.demoiselle.jee.multitenancy.hibernate.dao.MultiTenantProvider" />
    </properties>
</persistence-unit>

<persistence-unit name="UserMasterPU" transaction-type="JTA">
    <provider>org.hibernate.ejb.HibernatePersistence</provider>
    <jta-data-source>java:jboss/datasources/UserMasterDS</jta-data-source>
    <class>org.demoiselle.jee.multitenancy.hibernate.entity.Tenant</class>
    <exclude-unlisted-classes>true</exclude-unlisted-classes>
    <properties>
        <property name="hibernate.dialect" value="org.hibernate.dialect.MySQLDialect" />
        <property name="hibernate.hbm2ddl.auto" value="update" />
        <property name="hibernate.show_sql" value="false" />		
    </properties>
</persistence-unit>

<persistence-unit name="UserExportPU" transaction-type="JTA">
    <provider>org.hibernate.ejb.HibernatePersistence</provider>
    <jta-data-source>java:jboss/datasources/UserTenantsDS</jta-data-source>
    <class>org.demoiselle.jee7.example.store.user.entity.User</class>
    <exclude-unlisted-classes>true</exclude-unlisted-classes>
    <properties>
        <property name="hibernate.dialect" value="org.hibernate.dialect.MySQLDialect" />
        <property name="javax.persistence.schema-generation.scripts.action"
            value="drop-and-create" />
        <property name="javax.persistence.schema-generation.scripts.create-target"
            value="../standalone/tmp/demoiselleMultiTenantCreate_User.ddl" />
        <property name="javax.persistence.schema-generation.scripts.drop-target"
            value="../standalone/tmp/demoiselleMultiTenantDrop_User.ddl" />
    </properties>
</persistence-unit>
```