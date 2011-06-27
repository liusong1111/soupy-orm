Design
=======

## overview

build a query, connect to a repository(connection string and adapter), given a mapper, then execute.
finally, get list of model instance.

## TODOs

### model操作
* (OK) 设置主键
* (OK) insert
* (OK) update(by主键)
* (OK) destroy(by主键)


### oracle支持
* (OK) sequence支持[通过AppFrame]

### appframe支持
* (OK) maven打包支持
* (OK) Service接入-EJB环境
* (OK) ConnectionProvider接入
* (OK) 通用Service接入-使得内置的方法可以直接使用，内部自动包上Service。
* (OK) getNewId接入
* (OK) view.TableFor封装


### 通用
* (OK) 配置
* (OK) sql日志
