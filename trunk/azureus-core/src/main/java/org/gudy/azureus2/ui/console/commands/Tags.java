package org.gudy.azureus2.ui.console.commands;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.gudy.azureus2.core3.download.DownloadManager;
import org.gudy.azureus2.core3.util.Debug;
import org.gudy.azureus2.ui.console.ConsoleInput;

import com.aelitis.azureus.core.tag.Tag;
import com.aelitis.azureus.core.tag.TagFeatureRSSFeed;
import com.aelitis.azureus.core.tag.TagManager;
import com.aelitis.azureus.core.tag.TagManagerFactory;
import com.aelitis.azureus.core.tag.TagType;

public class Tags extends IConsoleCommand {

    private List<Tag> current_tags;
    private Tag current_tag;

    public Tags() {
        super("tag", "tag");
    }

    public String getCommandDescriptions() {
        return ("tags\t\tAccess to tags.");
    }

    public void printHelpExtra(PrintStream out, List<String> args) {
        out.println("> -----");
        out.println("Subcommands:");
        out.println("\tlist\t: List tags");
        out.println("\tcreate <name>\t: Create a new tag");
        out.println("\tselect <number>\t: Select tag <number> for further operations");
        out.println("The following commands operate on a selected tag");
        out.println("\ttorrents\t: List the tag's torrents");
        out.println("\tshow\t: Show tag properties");
        out.println("\tset_rssenable [yes|no]\t: Enable/disable RSS feed generation for the tag");
        out.println("\tdelete\t: Delete the tag");

        out.println("> -----");
    }

    public void execute(String commandName, ConsoleInput ci, List<String> args) {

        if (args.isEmpty()) {

            printHelp(ci.out, args);

            return;
        }

        String cmd = args.get(0);

        TagManager tm = TagManagerFactory.getTagManager();

        TagType tt = tm.getTagType(TagType.TT_DOWNLOAD_MANUAL);

        if (cmd.equals("list")) {

            ci.out.println("> -----");

            current_tags = tt.getTags();

            int index = 1;

            for (Tag tag : current_tags) {

                String index_str = "" + index++;

                while (index_str.length() < 3) {
                    index_str += " ";
                }

                String str = index_str + tag.getTagName(true) + ", downloads=" + tag.getTaggedCount();

                ci.out.println(str);
            }

            if (current_tags.size() == 0) {

                ci.out.println("No Tags");
            }
        } else if (cmd.equals("create")) {

            if (args.size() < 2) {

                ci.out.println("Usage: tag create <name>");

            } else {

                String tag_name = args.get(1);

                if (tt.getTag(tag_name, true) != null) {

                    ci.out.println("Tag already exists");

                } else {
                    try {
                        tt.createTag(tag_name, true);

                        ci.out.println("Tag created");

                    } catch (Throwable e) {

                        ci.out.println("Failed to create tag: " + Debug.getNestedExceptionMessage(e));
                    }
                }
            }
        } else if (cmd.equals("select")) {

            if (args.size() < 2) {

                ci.out.println("Usage: tag select <number>");

            } else {

                try {
                    int index = Integer.parseInt(args.get(1));

                    if (current_tags == null) {

                        throw (new Exception("tags must be listed prior to being selected"));

                    } else if (current_tags.size() == 0) {

                        throw (new Exception("no tags exist"));

                    } else if (index < 0 || index > current_tags.size()) {

                        throw (new Exception("tag index '" + index + "' is out of range"));

                    } else {
                        current_tag = current_tags.get(index - 1);

                        ci.out.println("Selected tag '" + current_tag.getTagName(true) + "'");
                    }
                } catch (Throwable e) {

                    ci.out.println("Failed to select tag: " + Debug.getNestedExceptionMessage(e));

                }
            }

        } else if (cmd.equals("torrents") || cmd.equals("show") || cmd.equals("delete") || cmd.equals("set_rssenable")) {

            if (current_tag == null) {

                ci.out.println("No current tag - select one!");

            } else {

                if (cmd.equals("torrents")) {

                    ci.out.println("Torrents for tag '" + current_tag.getTagName(true) + "'");

                    List<DownloadManager> downloads =
                            new ArrayList<DownloadManager>((java.util.Set<DownloadManager>) (Object) current_tag.getTagged());

                    Collections.sort(downloads, new TorrentComparator());

                    for (DownloadManager dm : downloads) {

                        ci.out.println(getTorrentSummary(dm));
                    }

                } else if (cmd.equals("show")) {

                    ci.out.println("Details for tag '" + current_tag.getTagName(true) + "'");

                    ci.out.println("\tRSS Enable: " + ((TagFeatureRSSFeed) current_tag).isTagRSSFeedEnabled());

                } else if (cmd.equals("set_rssenable")) {

                    if (args.size() < 2) {

                        ci.out.println("Usage: " + cmd + " [yes|no]");

                    } else {

                        String temp = args.get(1);

                        if (temp.equals("yes") || temp.equals("no")) {

                            ((TagFeatureRSSFeed) current_tag).setTagRSSFeedEnabled(temp.equals("yes"));

                        } else {

                            ci.out.println("Usage: " + cmd + " [yes|no]");
                        }
                    }
                } else if (cmd.equals("delete")) {

                    current_tag.removeTag();

                    current_tag = null;
                    current_tags = null;
                }
            }
        } else {

            ci.out.println("Unsupported sub-command: " + cmd);
        }

        ci.out.println("> -----");
    }
}
