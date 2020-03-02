package com.service.applehip.util

/**
 * Graphql용 쿼리 빌더
 * @param type 쿼리의 종류, QUERY 혹은 MUTATION
 */
class GraphqlQueryBuilder(private val type: GraphqlType) {

    private var method : GraphqlMethod? = null
    private var response : GraphqlResponse = GraphqlResponse()

    /**
     * Graphql 메소드 이름 설정
     */
    fun method(methodName : String) : GraphqlQueryBuilder {
        if(method == null && methodName != "") {
            this.method = GraphqlMethod(methodName)
        }
        return this
    }

    /**
     * param이 int형일때
     */
    fun methodParam(name : String, value : Int) : GraphqlQueryBuilder {
        this.method!!.setParam(name, value)
        return this
    }

    /**
     * param이 Long형일때
     */
    fun methodParam(name : String, value : Long) : GraphqlQueryBuilder {
        this.method!!.setParam(name, value)
        return this
    }

    /**
     * param이 String일때
     */
    fun methodParam(name : String, value : String) : GraphqlQueryBuilder {
        this.method!!.setParam(name, value)
        return this
    }

    /**
     * param이 특정 객체일때
     */
    fun methodParam(name : String, value : GraphqlMethodParam) : GraphqlQueryBuilder {
        this.method!!.setParam(name, value)
        return this
    }

    /**
     * 결과를 받을때 받을 내용을 설정
     */
    fun responseList(vararg responseList : String): GraphqlQueryBuilder {
        responseList.forEach {
            response.setResponse(it)
        }
        return this
    }

    /**
     * 실제 쿼리 반환
     */
    fun build() : String {
        return "${type.type} {$method $response}"
    }

    /**
     * 메소드를 담당하는 파트
     */
    private class GraphqlMethod(private val methodName: String) {

        private val paramList = ArrayList<String>()

        fun setParam(name : String, value : Int) = paramList.add("$name : $value")
        fun setParam(name : String, value : Long) = paramList.add("$name : $value")
        fun setParam(name : String, value : String) = paramList.add("$name : \"$value\"")
        fun setParam(name : String, value : GraphqlMethodParam) = paramList.add("$name : {$value}")

        override fun toString() : String {
            var result = methodName
            //파라미터가 없다면, 메소드명만 반환
            if(paramList.size == 0) {
                return result
            }

            // 파라미터가 있다면 해당 파라미터들을 감쌈.
            result += "("
            var inner = ""
            paramList.forEach {
                inner += ","
                inner += it
            }
            if(inner != "") {
                result += inner.substring(1)
            }
            result += ")"

            return result
        }
    }



    /**
     * response를 관리하는 class
     */
    private class GraphqlResponse {

        private val responseList = ArrayList<String>()

        fun setResponse(response : String) = this.responseList.add(response)

        override fun toString(): String {
            if(responseList.size == 0) {
                return ""
            }
            var result = ""
            for (response in responseList) {
                result += ",$response"
            }
            return "{" + result.substring(1) + "}"
        }
    }
}



/**
 * 메소드 파라미터 전용
 */
class GraphqlMethodParam {
    private val paramList = ArrayList<String>()

    fun setParam(name : String, value : Int) : GraphqlMethodParam {
        paramList.add("$name : $value")
        return this
    }

    fun setParam(name : String, value : Long) : GraphqlMethodParam {
        paramList.add("$name : $value")
        return this
    }

    fun setParam(name : String, value : String): GraphqlMethodParam {
        paramList.add("$name : \"$value\"")
        return this
    }

    override fun toString(): String {
        var result = ""
        if(paramList.size == 0) {
            return result
        }
        for (param in paramList) {
            result += ",$param"
        }

        return result.substring(1)
    }
}

enum class GraphqlType(val type : String) {
    QUERY("query"),
    MUTATION("mutation")
}