package com.water.waterreminder.pojos;

/**
 * Created by FreakyCoder on 27/08/15.
 */


//LePOJO of Devs
public class Devs {

    private int dev_logo;
    private String dev_name;
    private String dev_job;
    private int facebook_logo;
    private int twitter_logo;
    private int linked_in_logo;
    private int github_logo;

    private String dev_facebook_link;
    private String dev_twitter_link;
    private String dev_linkedin_link;
    private String dev_github_link;

    //Constructor
    public Devs(int dev_logo, String dev_name, String dev_job, int facebook_logo, int twitter_logo, int linked_in_logo, int github_logo, String dev_facebook_link, String dev_twitter_link, String dev_linkedin_link, String dev_github_link) {
        this.dev_logo = dev_logo;
        this.dev_name = dev_name;
        this.dev_job = dev_job;
        this.facebook_logo = facebook_logo;
        this.twitter_logo = twitter_logo;
        this.linked_in_logo = linked_in_logo;
        this.github_logo = github_logo;
        this.dev_facebook_link = dev_facebook_link;
        this.dev_twitter_link = dev_twitter_link;
        this.dev_linkedin_link = dev_linkedin_link;
        this.dev_github_link = dev_github_link;
    }

    //Getters and Setters
    public int getDev_logo() {
        return dev_logo;
    }

    public void setDev_logo(int dev_logo) {
        this.dev_logo = dev_logo;
    }

    public String getDev_name() {
        return dev_name;
    }

    public void setDev_name(String dev_name) {
        this.dev_name = dev_name;
    }

    public String getDev_job() {
        return dev_job;
    }

    public void setDev_job(String dev_job) {
        this.dev_job = dev_job;
    }

    public String getDev_facebook_link() {
        return dev_facebook_link;
    }

    public void setDev_facebook_link(String dev_facebook_link) {
        this.dev_facebook_link = dev_facebook_link;
    }

    public String getDev_twitter_link() {
        return dev_twitter_link;
    }

    public void setDev_twitter_link(String dev_twitter_link) {
        this.dev_twitter_link = dev_twitter_link;
    }

    public String getDev_linkedin_link() {
        return dev_linkedin_link;
    }

    public void setDev_linkedin_link(String dev_linkedin_link) {
        this.dev_linkedin_link = dev_linkedin_link;
    }

    public String getDev_github_link() {
        return dev_github_link;
    }

    public void setDev_github_link(String dev_github_link) {
        this.dev_github_link = dev_github_link;
    }

    public int getFacebook_logo() {
        return facebook_logo;
    }

    public void setFacebook_logo(int facebook_logo) {
        this.facebook_logo = facebook_logo;
    }

    public int getTwitter_logo() {
        return twitter_logo;
    }

    public void setTwitter_logo(int twitter_logo) {
        this.twitter_logo = twitter_logo;
    }

    public int getLinked_in_logo() {
        return linked_in_logo;
    }

    public void setLinked_in_logo(int linked_in_logo) {
        this.linked_in_logo = linked_in_logo;
    }

    public int getGithub_logo() {
        return github_logo;
    }

    public void setGithub_logo(int github_logo) {
        this.github_logo = github_logo;
    }
}
