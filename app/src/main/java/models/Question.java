package models;

public class Question {
    private String questionId,productId,name,question,answer,date;

    public Question() {
    }

    public Question(String questionId, String productId, String name, String question, String answer, String date) {
        this.questionId = questionId;
        this.productId=productId;
        this.name = name;
        this.question = question;
        this.answer=answer;
        this.date=date;
    }

    public Question(String name, String question,String answer,String date) {
        this.name = name;
        this.question = question;
        this.answer=answer;
        this.date=date;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
